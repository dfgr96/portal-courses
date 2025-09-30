package com.portal.auth.infraestructure.repository;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", schema = "db_portalcursos")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String provider;
    private String providerId;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}
