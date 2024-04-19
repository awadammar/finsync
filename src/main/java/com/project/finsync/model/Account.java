package com.project.finsync.model;

import com.project.finsync.enums.AccountType;
import com.project.finsync.validation.EnumValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

@Data
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    public static final String ACCOUNT_DEFAULT_NAME = "Default Account";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = AccountType.class)
    private AccountType type;

    @Positive(message = "Account number must be a positive value")
    @Column(name = "account_number")
    private String accountNo;

    @Pattern(regexp = "\\b[A-Z]{3}\\b", message = "Currency must be a valid 3 capital letters")
    private Currency currency;

    public Account(User user) {
        this.user = user;
        this.accountNo = ACCOUNT_DEFAULT_NAME;
        this.type = AccountType.PERSONAL;
    }

    public Account(User user, String accountNo) {
        this.user = user;
        this.accountNo = accountNo;
    }

    public Account(User user, String accountNo, AccountType type, Currency currency) {
        this.user = user;
        this.accountNo = accountNo;
        this.type = type;
        this.currency = currency;
    }
}
