package com.project.finsync.model;

import com.project.finsync.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
@Table(name = "accounts")
@Entity
public class Account {
    public static final String ACCOUNT_DEFAULT_NAME = "Default Account";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountType type;
    private String number;
    private Currency currency;

    public Account(User user) {
        this.user = user;
        this.number = ACCOUNT_DEFAULT_NAME;
        this.type = AccountType.PERSONAL;
    }

    public Account(User user, String number, AccountType type, Currency currency) {
        this.user = user;
        this.number = number;
        this.type = type;
        this.currency = currency;
    }
}
