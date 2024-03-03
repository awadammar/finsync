package com.project.finsync.model;

import com.project.finsync.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "accounts")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String accountName;
    private String currency;
}
