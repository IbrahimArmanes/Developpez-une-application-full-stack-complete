package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.PostSimpleDto;
import com.openclassrooms.mddapi.dto.UserSimpleDto;
import com.openclassrooms.mddapi.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    /**
     * Convert Comment entity to CommentDto
     * @param comment the entity to convert
     * @return the DTO
     */
    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContenu(comment.getContenu());
        commentDto.setDate(comment.getDate());
        
        // Map author
        if (comment.getAuteur() != null) {
            UserSimpleDto auteurDto = new UserSimpleDto();
            auteurDto.setId(comment.getAuteur().getId());
            auteurDto.setUsername(comment.getAuteur().getUsername());
            commentDto.setAuteur(auteurDto);
        }
        
        // Map post
        if (comment.getArticle() != null) {
            PostSimpleDto articleDto = new PostSimpleDto();
            articleDto.setId(comment.getArticle().getId());
            articleDto.setTitre(comment.getArticle().getTitre());
            articleDto.setDate(comment.getArticle().getDate());
            commentDto.setArticle(articleDto);
        }
        
        return commentDto;
    }

    /**
     * Convert CommentDto to Comment entity
     * @param commentDto the DTO to convert
     * @return the entity
     */
    public Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setContenu(commentDto.getContenu());
        
        // Date and relationships (author, post) are set in the service
        
        return comment;
    }
}
