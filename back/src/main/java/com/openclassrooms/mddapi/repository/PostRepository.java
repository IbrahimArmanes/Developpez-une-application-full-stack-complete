package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Find all posts for a specific subject
     * @param subjectId the subject ID
     * @return list of posts
     */
    List<Post> findByThemeId(Long subjectId);
}
