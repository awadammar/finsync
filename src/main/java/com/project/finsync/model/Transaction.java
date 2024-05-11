package com.project.finsync.model;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.validation.EnumValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = TransactionType.class)
    private TransactionType type;

    @Positive(message = "Budget amount must be a positive value")
    private Double amount;

    @PastOrPresent(message = "Transaction date must be less than or equal today")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = ExpenseCategory.class)
    private ExpenseCategory category;

    private String description;

    private Point location;

    private Set<String> tags = new HashSet<>();


    public Transaction(Account account, Double amount, LocalDate date, TransactionType type, String description) {
        this.account = account;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public Transaction(Account account, Double amount, LocalDate date, TransactionType type, ExpenseCategory category) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = "";
    }
}
