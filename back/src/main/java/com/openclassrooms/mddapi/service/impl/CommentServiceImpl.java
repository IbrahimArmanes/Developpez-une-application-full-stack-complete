package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Find the post
        Post post = postRepository.findById(commentDto.getArticle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + commentDto.getArticle().getId()));
        
        // Convert DTO to entity
        Comment comment = commentMapper.toEntity(commentDto);
        
        // Set the author, post, and date
        comment.setAuteur(user);
        comment.setArticle(post);
        comment.setDate(LocalDateTime.now());
        
        // Save the entity
        Comment savedComment = commentRepository.save(comment);
        
        // Convert back to DTO and return
        return commentMapper.toDto(savedComment);
    }
    

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long id) {
        // Find comment by ID or throw exception if not found
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        
        // Convert to DTO and return
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(CommentDto commentDto) {
        // Check if comment exists
        Comment existingComment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentDto.getId()));
        
        // Verify that the current user is the author of the comment
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        if (!existingComment.getAuteur().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to update this comment");
        }
        
        // Update fields
        existingComment.setContenu(commentDto.getContenu());
        
        // Save the updated entity
        Comment updatedComment = commentRepository.save(existingComment);
        
        // Convert back to DTO and return
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        // Check if comment exists
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        
        // Verify that the current user is the author of the comment
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        if (!comment.getAuteur().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }
        
        // Delete the comment
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        // Check if post exists
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        
        // Get all comments for the post
        List<Comment> comments = commentRepository.findByArticleId(postId);
        
        // Convert to DTOs and return
        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
