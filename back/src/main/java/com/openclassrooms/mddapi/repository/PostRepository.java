package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Find all posts for a specific subject
     * @param subjectId the subject ID
     * @return list of posts
     */
    List<Post> findByThemeId(Long subjectId);
        
    /**
     * Find all posts for a set of subjects ordered by date ascending
     * @param themes set of subjects
     * @return list of posts
     */
    List<Post> findByThemeInOrderByDateAsc(Collection<Subject> themes);
    
    /**
     * Find all posts for a set of subjects ordered by date descending
     * @param themes set of subjects
     * @return list of posts
     */
    List<Post> findByThemeInOrderByDateDesc(Collection<Subject> themes);
}
