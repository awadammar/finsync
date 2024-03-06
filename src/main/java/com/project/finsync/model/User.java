package com.project.finsync.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password; // Note: In production, use proper encryption/hashing

    public User(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}