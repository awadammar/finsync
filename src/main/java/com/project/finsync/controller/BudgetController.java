package com.project.finsync.controller;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/users/{userId}/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    public Iterable<Budget> findBudgetsForUser(@PathVariable Long userId) {
        return budgetService.findBudgetsByUser(userId);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> findBudgetByIdForUser(@PathVariable Long budgetId, @PathVariable Long userId) {
        return budgetService.findBudgetByIdAndUser(budgetId, userId)
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/month/{month}")
    public Iterable<Budget> findBudgetsByMonthForUser(@PathVariable Long userId, @PathVariable Month month) {
        return budgetService.findBudgetsByUserByMonth(userId, month);
    }

    @GetMapping("/category/{category}")
    public Iterable<Budget> findBudgetByCategoryForUser(@PathVariable Long userId, @PathVariable ExpenseCategory category) {
        return budgetService.findBudgetsByUserByCategory(userId, category);
    }

    @GetMapping("/total-amount")
    public Double findTotalAmountForBudgets(@PathVariable Long userId) {
        return budgetService.findTotalAmountOfBudgets(userId);
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@PathVariable Long userId, @Valid @RequestBody Budget budget) {
        return budgetService.createBudget(userId, budget)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long budgetId, @PathVariable Long userId, @Valid @RequestBody Budget updateBudget) {
        return budgetService.updateBudget(budgetId, userId, updateBudget)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<Void> deleteAllBudgetsByCategoryForUser(@PathVariable Long userId, @PathVariable ExpenseCategory category) {
        budgetService.deleteAllBudgetsByUserByCategory(userId, category);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId, @PathVariable Long userId) {
        budgetService.deleteBudget(budgetId, userId);
        return ResponseEntity.noContent().build();
    }
}
