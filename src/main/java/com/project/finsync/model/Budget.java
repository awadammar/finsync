package com.project.finsync.model;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.validation.EnumValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Positive(message = "Budget amount must be a positive value")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = Month.class)
    private Month month;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = ExpenseCategory.class)
    private ExpenseCategory category;

    public Budget(User user, Double amount, Month month, ExpenseCategory category) {
        this.user = user;
        this.amount = amount;
        this.month = month;
        this.category = category;
    }
}
