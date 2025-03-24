package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserCreateDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;

public interface UserService {
    
    /**
     * Create a new user
     * @param userCreateDto User creation data
     * @return Created user response
     */
    UserResponseDto createUser(UserCreateDto userCreateDto);
    
    /**
     * Get user by ID
     * @param id User ID
     * @return User data
     */
    UserDto getUserById(Long id);
    
    /**
     * Get user by username
     * @param username Username
     * @return User data
     */
    UserDto getUserByUsername(String username);
    
    /**
     * Update user
     * @param userDto Updated user data
     * @return Updated user
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Update user password
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     */
    void updatePassword(Long userId, String currentPassword, String newPassword);
}
