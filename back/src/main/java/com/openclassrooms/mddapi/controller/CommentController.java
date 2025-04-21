package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Create a new comment
     * @param commentDto the comment data
     * @return the created comment
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        try {
            // Validate that the required fields are present
            if (commentDto.getContenu() == null || commentDto.getContenu().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (commentDto.getArticle() == null || commentDto.getArticle().getId() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create the comment
            CommentDto createdComment = commentService.createComment(commentDto);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
