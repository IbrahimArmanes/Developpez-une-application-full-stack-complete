package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Find all comments for a specific post
     * @param postId the post ID
     * @return list of comments
     */
    List<Comment> findByArticleId(Long postId);
}
