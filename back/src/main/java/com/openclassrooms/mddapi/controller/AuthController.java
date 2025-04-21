package com.openclassrooms.mddapi.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.auth.JwtResponse;
import com.openclassrooms.mddapi.dto.auth.LoginRequest;
import com.openclassrooms.mddapi.dto.auth.MessageResponse;
import com.openclassrooms.mddapi.dto.auth.RegisterRequest;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        UserRepository userRepository;

        @Autowired
        PasswordEncoder encoder;

        @Autowired
        JwtUtils jwtUtils;
    
        @Value("${app.jwt.expiration}")
        private int jwtExpirationMs;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);
            
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                // Create a cookie with the JWT token
                Cookie jwtCookie = new Cookie("token", jwt);
                jwtCookie.setMaxAge(jwtExpirationMs / 1000); // Convert ms to seconds
                jwtCookie.setPath("/");
                jwtCookie.setHttpOnly(true);
            
                // In production, you would add:
                // jwtCookie.setSecure(true);
            
                // Add the cookie to the response
                response.addCookie(jwtCookie);

                // Return user details without the token in the body
                return ResponseEntity.ok(new JwtResponse(
                                                    userDetails.getId(),
                                                    userDetails.getUsername(),
                                                    userDetails.getEmail()));
            } catch (Exception e) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Nom d'utilisateur ou mot de passe incorrect"));
            }
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logoutUser(HttpServletResponse response) {
            // Create a cookie that expires immediately to clear the token
            Cookie jwtCookie = new Cookie("token", null);
            jwtCookie.setMaxAge(0);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);
        
            response.addCookie(jwtCookie);
        
            return ResponseEntity.ok(new MessageResponse("Déconnexion réussie"));
        }

        @PostMapping("/register")
        public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Erreur: Ce nom d'utilisateur est déjà pris!"));
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Erreur: Cet email est déjà utilisé!"));
            }

            // Créer un nouvel utilisateur
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setAbonnements(new HashSet<>());
            user.setPosts(new HashSet<>());
            user.setComments(new HashSet<>());

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
        }
}