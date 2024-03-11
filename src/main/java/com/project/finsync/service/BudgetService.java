package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.model.User;
import com.project.finsync.repository.BudgetRepository;
import com.project.finsync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public Iterable<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found with id: " + budgetId));
    }

    public List<Budget> getBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public List<Budget> getBudgetsByUserByMonth(User user, Month month) {
        return budgetRepository.findByUserAndMonth(user, month);
    }

    public List<Budget> getBudgetByUserByCategory(User user, ExpenseCategory category) {
        return budgetRepository.findByUserAndCategory(user, category);
    }

    public Double getTotalAmountForBudgets(User user) {
        return budgetRepository.findByUser(user)
                .stream()
                .mapToDouble(Budget::getAmount)
                .sum();
    }

}
