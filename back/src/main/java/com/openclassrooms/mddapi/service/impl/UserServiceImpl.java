package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.SubjectSimpleDto;
import com.openclassrooms.mddapi.dto.UserCreateDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Create new user
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setAbonnements(new HashSet<>());
        user.setPosts(new HashSet<>());
        user.setComments(new HashSet<>());

        User savedUser = userRepository.save(user);
        
        // Convert to DTO and return
        return convertToUserResponseDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return convertToUserDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return convertToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        // Get the authenticated user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        String username;
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        // Find the authenticated user in the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        // Check if email is being changed and if it's already in use
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Check if username is being changed and if it's already taken
        if (!user.getUsername().equals(userDto.getUsername()) && 
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Update ONLY email and username fields
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        
        // Save the updated user
        User updatedUser = userRepository.save(user);
        
        // Convert to DTO and return
        return convertToUserDto(updatedUser);
    }

    // Helper methods to convert between entities and DTOs
    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        
        // Convert abonnements to SubjectSimpleDto
        Set<SubjectSimpleDto> subjectSimpleDtos = new HashSet<>();
        if (user.getAbonnements() != null) {
            for (Subject subject : user.getAbonnements()) {
                SubjectSimpleDto subjectSimpleDto = new SubjectSimpleDto();
                subjectSimpleDto.setId(subject.getId());
                subjectSimpleDto.setNom(subject.getNom());
                subjectSimpleDtos.add(subjectSimpleDto);
            }
        }
        userDto.setAbonnements(subjectSimpleDtos);
        
        return userDto;
    }

    private UserResponseDto convertToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        
        // Convert abonnements to SubjectSimpleDto
        Set<SubjectSimpleDto> subjectSimpleDtos = new HashSet<>();
        if (user.getAbonnements() != null) {
            for (Subject subject : user.getAbonnements()) {
                SubjectSimpleDto subjectSimpleDto = new SubjectSimpleDto();
                subjectSimpleDto.setId(subject.getId());
                subjectSimpleDto.setNom(subject.getNom());
                subjectSimpleDtos.add(subjectSimpleDto);
            }
        }
        userResponseDto.setAbonnements(subjectSimpleDtos);
        
        return userResponseDto;
    }
    
    @Override
    public void updatePassword(String currentPassword, String newPassword) {
        // Get the authenticated user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        String username;
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        // Find the authenticated user in the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUserProfile() {
        // Get the authenticated user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        
        String username;
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        // Find the user in the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        // The transaction will ensure that the lazy-loaded abonnements are loaded when accessed
        
        // Convert to DTO and return
        return convertToUserDto(user);
    }
}
