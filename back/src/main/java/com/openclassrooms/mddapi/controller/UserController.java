package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserCreateDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.dto.UserResponseDto;
import com.openclassrooms.mddapi.dto.auth.MessageResponse;
import com.openclassrooms.mddapi.dto.auth.PasswordUpdateRequest;
import com.openclassrooms.mddapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Create a new user
     * @param userCreateDto User creation data
     * @return The created user
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        try {
            UserResponseDto createdUser = userService.createUser(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", e);
        }
    }

    /**
     * Get user by ID
     * @param id User ID
     * @return User data
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user", e);
        }
    }

    /**
     * Get user by username
     * @param username Username
     * @return User data
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user", e);
        }
    }

    /**
     * Update user
     * @param id User ID
     * @param userDto Updated user data
     * @return Updated user
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and (authentication.principal.id == #id or hasRole('ADMIN'))")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        try {
            // Mettre à jour les attributs avec les valeurs du DTO
            userDto.setId(id); // S'assurer que l'ID est correctement défini
            
            UserDto updatedUser = userService.updateUser(userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user", e);
        }
    }


    /**
     * Update user password
     * @param id User ID
     * @param passwordUpdateRequest Password update request
     * @return Success message
     */
    @PutMapping("/{id}/password")
    @PreAuthorize("isAuthenticated() and authentication.principal.id == #id")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id, 
            @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        try {
            userService.updatePassword(id, 
                                    passwordUpdateRequest.getCurrentPassword(), 
                                    passwordUpdateRequest.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating password", e);
        }
    }

}
