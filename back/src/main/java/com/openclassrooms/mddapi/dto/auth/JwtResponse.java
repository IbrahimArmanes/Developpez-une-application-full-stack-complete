package com.openclassrooms.mddapi.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private Long id;
    private String username;
    private String email;
    private String token; // Keep this for backward compatibility but don't use it

    public JwtResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public JwtResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = null; // Not used when using cookies
    }
}