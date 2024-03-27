package com.project.finsync.model;

import com.project.finsync.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Month;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double amount;
    private Month month;
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    public Budget(User user, Double amount, Month month, ExpenseCategory category) {
        this.user = user;
        this.amount = amount;
        this.month = month;
        this.category = category;
    }
}
