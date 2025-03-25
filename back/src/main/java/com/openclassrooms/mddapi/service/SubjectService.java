package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.SubjectSimpleDto;

import java.util.List;

public interface SubjectService {
    /**
     * Create a new subject
     * @param subjectDto the subject to create
     * @return the created subject
     */
    SubjectDto createSubject(SubjectDto subjectDto);

    /**
     * Get all subjects
     * @return list of all subjects in simple form
     */
    List<SubjectSimpleDto> getAllSubjects();

    /**
     * Get a subject by its ID
     * @param id the subject ID
     * @return the subject
     */
    SubjectDto getSubjectById(Long id);

    /**
     * Update a subject
     * @param subjectDto the updated subject data
     * @return the updated subject
     */
    SubjectDto updateSubject(SubjectDto subjectDto);

    /**
     * Delete a subject by its ID
     * @param id the subject ID
     */
    void deleteSubject(Long id);
}
