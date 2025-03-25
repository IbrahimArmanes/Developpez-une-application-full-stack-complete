package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDto;

import java.util.List;

public interface CommentService {
    /**
     * Create a new comment
     * @param commentDto the comment to create
     * @return the created comment
     */
    CommentDto createComment(CommentDto commentDto);

    /**
     * Get a comment by its ID
     * @param id the comment ID
     * @return the comment
     */
    CommentDto getCommentById(Long id);

    /**
     * Update a comment
     * @param commentDto the updated comment data
     * @return the updated comment
     */
    CommentDto updateComment(CommentDto commentDto);

    /**
     * Delete a comment by its ID
     * @param id the comment ID
     */
    void deleteComment(Long id);

    /**
     * Get all comments for a specific post
     * @param postId the post ID
     * @return list of comments for the post
     */
    List<CommentDto> getCommentsByPost(Long postId);
}
