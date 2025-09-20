package com.portal.auth.domain.model;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String email;
    private String name;
    private String provider;
    private String providerId;
    private String passwordHash;
    private Role role;

    public enum Role {
        USER, ADMIN
    }

    public User(Long id, String email, String name, String provider,
                String providerId, String passwordHash, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
