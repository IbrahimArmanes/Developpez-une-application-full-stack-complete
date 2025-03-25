package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.PostDto;
import com.openclassrooms.mddapi.dto.PostSimpleDto;
import com.openclassrooms.mddapi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Create a new post
     * @param postDto the post to create
     * @return the created post
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Get all posts
     * @return list of all posts
     */
    @GetMapping
    public ResponseEntity<List<PostSimpleDto>> getAllPosts() {
        List<PostSimpleDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /**
     * Get a post by its ID
     * @param id the post ID
     * @return the post
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto post = postService.getPostById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * Update a post
     * @param id the post ID
     * @param postDto the updated post data
     * @return the updated post
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        postDto.setId(id); // Ensure the ID in the path is used
        PostDto updatedPost = postService.updatePost(postDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    /**
     * Delete a post by its ID
     * @param id the post ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get all posts for a specific subject
     * @param subjectId the subject ID
     * @return list of posts for the subject
     */
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PostSimpleDto>> getPostsBySubject(@PathVariable Long subjectId) {
        List<PostSimpleDto> posts = postService.getPostsBySubject(subjectId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
