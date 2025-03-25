package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.model.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    /**
     * Convert Subject entity to SubjectDto
     * @param subject the entity to convert
     * @return the DTO
     */
    public SubjectDto toDto(Subject subject) {
        if (subject == null) {
            return null;
        }

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getId());
        subjectDto.setNom(subject.getNom());
        
        // Map related entities if needed
        // This would require additional mappers for Post and User entities
        
        return subjectDto;
    }

    /**
     * Convert SubjectDto to Subject entity
     * @param subjectDto the DTO to convert
     * @return the entity
     */
    public Subject toEntity(SubjectDto subjectDto) {
        if (subjectDto == null) {
            return null;
        }

        Subject subject = new Subject();
        subject.setId(subjectDto.getId());
        subject.setNom(subjectDto.getNom());
        
        // Map related DTOs if needed
        // This would require additional mappers for Post and User DTOs
        
        return subject;
    }
}
