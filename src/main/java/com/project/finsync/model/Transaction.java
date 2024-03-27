package com.project.finsync.model;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Double amount;
    private LocalDate date;
    private String description;
    private ExpenseCategory category;
    private Set<String> tags;
    private Point location;

    public Transaction(Account account, TransactionType type, Double amount, LocalDate date, ExpenseCategory category) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.description = "";
        this.tags = new HashSet<>();
    }
}
