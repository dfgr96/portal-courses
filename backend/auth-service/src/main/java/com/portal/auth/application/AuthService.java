package com.portal.auth.application;

import com.portal.auth.infraestructure.repository.TokenRepository;
import com.portal.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public AuthService(UserService userService, JwtUtil jwtUtil, TokenRepository tokenRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    public Optional<String> login(String email, String password) {
        return userService.getUserByEmail(email)
                .filter(user -> user.getPasswordHash() != null && user.getPasswordHash().equals(password))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getId().toString());
                    long expiresAt = jwtUtil.getExpiration(token).getTime();
                    tokenRepository.saveToken(token, user.getId().toString(), expiresAt);
                    return token;
                });
    }

}
