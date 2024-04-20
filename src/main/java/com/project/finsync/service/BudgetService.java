package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.repository.BudgetRepository;
import com.project.finsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public Optional<Budget> findBudgetByIdAndUser(Long budgetId, Long userId) {
        return budgetRepository.findByIdAndUserId(budgetId, userId);
    }

    public List<Budget> findBudgetsByUser(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    public List<Budget> findBudgetsByUserByMonth(Long userId, Month month) {
        return findBudgetsByUser(userId)
                .stream()
                .filter(budget -> month.equals(budget.getMonth()))
                .toList();
    }

    public List<Budget> findBudgetByUserByCategory(Long userId, ExpenseCategory expenseCategory) {
        return findBudgetsByUser(userId)
                .stream()
                .filter(budget -> expenseCategory.equals(budget.getCategory()))
                .toList();
    }

    public Double findTotalAmountForBudgets(Long userId) {
        return findBudgetsByUser(userId)
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

    public Optional<Budget> updateBudget(Long budgetId, Long userId, Budget updateBudget) {
        return findBudgetByIdAndUser(budgetId, userId).flatMap(budget -> {
            if (updateBudget.getAmount() != null) {
                budget.setAmount(updateBudget.getAmount());
            }
            return Optional.of(budgetRepository.save(budget));
        });
    }

    public void deleteAllBudgetsByUserByCategory(Long userId, ExpenseCategory expenseCategory) {
        List<Long> ids = budgetRepository.findByUserId(userId)
                .stream()
                .filter(budget -> budget.getCategory().equals(expenseCategory))
                .map(Budget::getId)
                .toList();
        budgetRepository.deleteAllById(ids);
    }

    public void deleteBudget(Long budgetId, Long userId) {
        findBudgetByIdAndUser(budgetId, userId).ifPresent(budget -> budgetRepository.deleteById(budgetId));
    }

}
