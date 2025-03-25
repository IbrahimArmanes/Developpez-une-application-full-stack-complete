package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.SubjectSimpleDto;
import com.openclassrooms.mddapi.dto.UserSimpleDto;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostMapper {

    /**
     * Convert Post entity to PostDto
     * @param post the entity to convert
     * @return the DTO
     */
    public PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitre(post.getTitre());
        postDto.setContenu(post.getContenu());
        postDto.setDate(post.getDate());
        
        // Map author
        if (post.getAuteur() != null) {
            UserSimpleDto auteurDto = new UserSimpleDto();
            auteurDto.setId(post.getAuteur().getId());
            auteurDto.setUsername(post.getAuteur().getUsername());
            postDto.setAuteur(auteurDto);
        }
        
        // Map subject
        if (post.getTheme() != null) {
            SubjectSimpleDto themeDto = new SubjectSimpleDto();
            themeDto.setId(post.getTheme().getId());
            themeDto.setNom(post.getTheme().getNom());
            postDto.setTheme(themeDto);
        }
        
        // Map comments
        if (post.getCommentaires() != null) {
            postDto.setCommentaires(post.getCommentaires().stream()
                    .map(this::mapCommentToDto)
                    .collect(Collectors.toSet()));
        }
        
        return postDto;
    }

    /**
     * Convert PostDto to Post entity
     * @param postDto the DTO to convert
     * @return the entity
     */
    public Post toEntity(PostDto postDto) {
        if (postDto == null) {
            return null;
        }

        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitre(postDto.getTitre());
        post.setContenu(postDto.getContenu());
        
        // Date and relationships (author, theme, comments) are set in the service
        
        return post;
    }
    
    /**
     * Helper method to map Comment to CommentDto
     * @param comment the comment to map
     * @return the comment DTO
     */
    private CommentDto mapCommentToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        // Set other comment properties as needed
        
        return commentDto;
    }
}
