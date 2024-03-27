package com.project.finsync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "E-mail address must not be empty")
    @Email(message = "User must have valid email address")
    private String email;
    @Column(nullable = false)
    private String password; // Note: In production, use proper encryption/hashing

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}