package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    // Additional query methods can be added here if needed
}
