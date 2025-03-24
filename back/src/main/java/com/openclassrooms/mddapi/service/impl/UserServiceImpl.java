package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.SubjectSimpleDto;
import com.openclassrooms.mddapi.dto.UserCreateDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDto updateUser(UserDto userDto) {
        // Check if user exists
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userDto.getId()));

        // Check if username is being changed and if it's already taken
        if (!user.getUsername().equals(userDto.getUsername()) && 
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if email is being changed and if it's already in use
        if (!user.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // Update user fields
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        
        // Note: We don't update password here as it should be handled by a separate endpoint
        // with proper validation

        User updatedUser = userRepository.save(user);
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
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
