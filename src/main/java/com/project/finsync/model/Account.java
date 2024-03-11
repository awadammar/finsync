package com.project.finsync.model;

import com.project.finsync.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private String name;
    private String currency;

    public Account(User user) {
        this.user = user;
        this.name = ACCOUNT_DEFAULT_NAME;
        this.type = AccountType.PERSONAL;
    }

    public Account(User user, String name, AccountType type) {
        this.user = user;
        this.name = name;
        this.type = type;
    }
}
