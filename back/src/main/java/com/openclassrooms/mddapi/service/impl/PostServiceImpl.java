package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.PostSimpleDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.PostService;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Find the subject
        Subject subject = subjectRepository.findById(postDto.getTheme().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + postDto.getTheme().getId()));
        
        // Convert DTO to entity
        Post post = postMapper.toEntity(postDto);
        
        // Set the author, theme, and date
        post.setAuteur(user);
        post.setTheme(subject);
        post.setDate(LocalDateTime.now());
        
        // Save the entity
        Post savedPost = postRepository.save(post);
        
        // Convert back to DTO and return
        return postMapper.toDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSimpleDto> getAllPosts() {
        // Get all posts from repository
        List<Post> posts = postRepository.findAll();
        
        // Convert to simple DTOs and return
        return posts.stream()
                .map(post -> {
                    PostSimpleDto simpleDto = new PostSimpleDto();
                    simpleDto.setId(post.getId());
                    simpleDto.setTitre(post.getTitre());
                    simpleDto.setDate(post.getDate());
                    return simpleDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        // Find post by ID or throw exception if not found
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        
        // Convert to DTO and return
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto updatePost(PostDto postDto) {
        // Check if post exists
        Post existingPost = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postDto.getId()));
        
        // Check if subject exists if it's being changed
        if (postDto.getTheme() != null && postDto.getTheme().getId() != null) {
            Subject subject = subjectRepository.findById(postDto.getTheme().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + postDto.getTheme().getId()));
            existingPost.setTheme(subject);
        }
        
        // Update fields
        existingPost.setTitre(postDto.getTitre());
        existingPost.setContenu(postDto.getContenu());
        
        // Save the updated entity
        Post updatedPost = postRepository.save(existingPost);
        
        // Convert back to DTO and return
        return postMapper.toDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        // Check if post exists
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        
        // Delete the post
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSimpleDto> getPostsBySubject(Long subjectId) {
        // Check if subject exists
        if (!subjectRepository.existsById(subjectId)) {
            throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
        }
        
        // Get all posts for the subject
        List<Post> posts = postRepository.findByThemeId(subjectId);
        
        // Convert to simple DTOs and return
        return posts.stream()
                .map(post -> {
                    PostSimpleDto simpleDto = new PostSimpleDto();
                    simpleDto.setId(post.getId());
                    simpleDto.setTitre(post.getTitre());
                    simpleDto.setDate(post.getDate());
                    return simpleDto;
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<PostSimpleDto> getFeed(String sortDirection) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        
        // Find the user with subscriptions
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Get user subscriptions
        Set<Subject> subscriptions = user.getAbonnements();
        
        // If user has no subscriptions, return empty list
        if (subscriptions == null || subscriptions.isEmpty()) {
            return List.of();
        }
        
        // Get posts from subscribed subjects with specified sort direction
        List<Post> posts;
        if ("asc".equalsIgnoreCase(sortDirection)) {
            posts = postRepository.findByThemeInOrderByDateAsc(subscriptions);
        } else {
            // Default is descending order
            posts = postRepository.findByThemeInOrderByDateDesc(subscriptions);
        }
        
        // Convert to simple DTOs and return
        return posts.stream()
                .map(post -> {
                    PostSimpleDto simpleDto = new PostSimpleDto();
                    simpleDto.setId(post.getId());
                    simpleDto.setTitre(post.getTitre());
                    simpleDto.setDate(post.getDate());
                    return simpleDto;
                })
                .collect(Collectors.toList());
    }

}
