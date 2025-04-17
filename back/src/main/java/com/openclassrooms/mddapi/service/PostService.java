package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.PostSimpleDto;

import java.util.List;

public interface PostService {
    /**
     * Create a new post
     * @param postDto the post to create
     * @return the created post
     */
    PostDto createPost(PostDto postDto);

    /**
     * Get all posts
     * @return list of all posts in simple form
     */
    List<PostSimpleDto> getAllPosts();

    /**
     * Get a post by its ID
     * @param id the post ID
     * @return the post
     */
    PostDto getPostById(Long id);

    /**
     * Update a post
     * @param postDto the updated post data
     * @return the updated post
     */
    PostDto updatePost(PostDto postDto);

    /**
     * Delete a post by its ID
     * @param id the post ID
     */
    void deletePost(Long id);

    /**
     * Get all posts for a specific subject
     * @param subjectId the subject ID
     * @return list of posts for the subject in simple form
     */
    List<PostSimpleDto> getPostsBySubject(Long subjectId);
    
    /**
     * Get personalized feed of posts based on user subscriptions
     * @param sortDirection the sort direction ("asc" or "desc")
     * @return list of posts from subscribed subjects
     */
    List<PostSimpleDto> getFeed(String sortDirection);
}