package com.moe.springbootsupabaseauth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at",updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    @Column(name = "email",unique = true,nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "enabled",nullable = false)
    private boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verificationexpiration")
    private LocalDateTime verificationExpiration;
}
