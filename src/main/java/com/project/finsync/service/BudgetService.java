package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.repository.BudgetRepository;
import com.project.finsync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public Iterable<Budget> findAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget findBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found with id: " + budgetId));
    }

    public List<Budget> findBudgetsByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(budgetRepository::findByUser)
                .orElse(Collections.emptyList());
    }

    public List<Budget> findBudgetsByUserByMonth(Long userId, Month month) {
        return findBudgetsByUserId(userId)
                .stream()
                .filter(budget -> month.equals(budget.getMonth()))
                .toList();
    }

    public List<Budget> findBudgetByUserByCategory(Long userId, ExpenseCategory expenseCategory) {
        return findBudgetsByUserId(userId)
                .stream()
                .filter(budget -> expenseCategory.equals(budget.getCategory()))
                .toList();
    }

    public Double findTotalAmountForBudgets(Long userId) {
        return findBudgetsByUserId(userId)
                .stream()
                .mapToDouble(Budget::getAmount)
                .sum();
    }

    public Optional<Budget> createBudget(Long userId, Budget budget) {
        return userRepository.findById(userId).map(user -> {
            budget.setUser(user);
            return budgetRepository.save(budget);
        });
    }

    public Optional<Budget> updateBudget(Long budgetId, Budget updateBudget) {
        return budgetRepository.findById(budgetId).map(budget -> {
            if (updateBudget.getAmount() != null) {
                budget.setAmount(updateBudget.getAmount());
            }
            return budgetRepository.save(budget);
        });
    }

    public void deleteBudgetsByCategory(Long userId, ExpenseCategory expenseCategory) {
        findBudgetByUserByCategory(userId, expenseCategory)
                .forEach(budget -> deleteBudget(budget.getBudgetId()));
    }

    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

}
