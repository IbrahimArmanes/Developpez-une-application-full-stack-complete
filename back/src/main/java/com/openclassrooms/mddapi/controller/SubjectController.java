package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.SubjectSimpleDto;
import com.openclassrooms.mddapi.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * Create a new subject
     * @param subjectDto the subject to create
     * @return the created subject
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto) {
        SubjectDto createdSubject = subjectService.createSubject(subjectDto);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    /**
     * Get all subjects
     * @return list of all subjects
     */
    @GetMapping
    public ResponseEntity<List<SubjectSimpleDto>> getAllSubjects() {
        List<SubjectSimpleDto> subjects = subjectService.getAllSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    /**
     * Get a subject by its ID
     * @param id the subject ID
     * @return the subject
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
        SubjectDto subject = subjectService.getSubjectById(id);
        return new ResponseEntity<>(subject, HttpStatus.OK);
    }

    /**
     * Update a subject
     * @param id the subject ID
     * @param subjectDto the updated subject data
     * @return the updated subject
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) {
        subjectDto.setId(id); // Ensure the ID in the path is used
        SubjectDto updatedSubject = subjectService.updateSubject(subjectDto);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    /**
     * Delete a subject by its ID
     * @param id the subject ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * Subscribe the authenticated user to a subject
     * @param id the subject ID to subscribe to
     * @return OK response
     */
    @PostMapping("/{id}/subscribe")
    @PreAuthorize("isAuthenticated()")
public ResponseEntity<Void> subscribeToSubject(@PathVariable Long id) {
    try {
        subjectService.subscribe(id);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
        // Log the detailed error
        System.err.println("Error subscribing to subject: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    
    /**
     * Unsubscribe the authenticated user from a subject
     * @param id the subject ID to unsubscribe from
     * @return OK response
     */
    @DeleteMapping("/{id}/subscribe")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unsubscribeFromSubject(@PathVariable Long id) {
        subjectService.unsubscribe(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
