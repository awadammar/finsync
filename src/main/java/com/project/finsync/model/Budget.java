package com.project.finsync.model;

import com.project.finsync.enums.BudgetCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    private String budgetName;
    private double budgetAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private BudgetCategory category;
}
