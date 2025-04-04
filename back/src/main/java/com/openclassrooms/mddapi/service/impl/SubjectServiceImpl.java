package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.SubjectDto;
import com.openclassrooms.mddapi.dto.SubjectSimpleDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        // Convert DTO to entity
        Subject subject = subjectMapper.toEntity(subjectDto);
        
        // Save the entity
        Subject savedSubject = subjectRepository.save(subject);
        
        // Convert back to DTO and return
        return subjectMapper.toDto(savedSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectSimpleDto> getAllSubjects() {
        // Get all subjects from repository
        List<Subject> subjects = subjectRepository.findAll();
        
        // Convert to simple DTOs and return
        return subjects.stream()
                .map(subject -> {
                    SubjectSimpleDto simpleDto = new SubjectSimpleDto();
                    simpleDto.setId(subject.getId());
                    simpleDto.setNom(subject.getNom());
                    return simpleDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDto getSubjectById(Long id) {
        // Find subject by ID or throw exception if not found
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
        
        // Convert to DTO and return
        return subjectMapper.toDto(subject);
    }

    @Override
    @Transactional
    public SubjectDto updateSubject(SubjectDto subjectDto) {
        // Check if subject exists
        if (!subjectRepository.existsById(subjectDto.getId())) {
            throw new ResourceNotFoundException("Subject not found with id: " + subjectDto.getId());
        }
        
        // Convert DTO to entity
        Subject subject = subjectMapper.toEntity(subjectDto);
        
        // Save the updated entity
        Subject updatedSubject = subjectRepository.save(subject);
        
        // Convert back to DTO and return
        return subjectMapper.toDto(updatedSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        // Check if subject exists
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id: " + id);
        }
        
        // Delete the subject
        subjectRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public void subscribe(Long subjectId) {
        // Get the authenticated user's ID from SecurityContextHolder
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        
        // Retrieve the User entity
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Retrieve the Subject entity
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        
        // Check if the user is already subscribed to avoid duplicates
        if (user.getAbonnements().contains(subject)) {
            return; // User is already subscribed, no action needed
        }
        
        // Add the subject to the user's subscriptions
        user.getAbonnements().add(subject);
        
        // Save the updated user entity
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unsubscribe(Long subjectId) {
        // Get the authenticated user's ID from SecurityContextHolder
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        
        // Retrieve the User entity
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Retrieve the Subject entity
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        
        // Check if the user is subscribed to the subject
        if (!user.getAbonnements().contains(subject)) {
            return; // User is not subscribed, no action needed
        }
        
        // Remove the subject from the user's subscriptions
        user.getAbonnements().remove(subject);
        
        // Save the updated user entity
        userRepository.save(user);
    }
}
