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
    private static final String ACCOUNT_DEFAULT_NAME = "Default Account";

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

    public Account(Long userId) {
        this.userId = userId;
        this.accountType = AccountType.PERSONAL;
        this.accountName = ACCOUNT_DEFAULT_NAME;
    }
}
