package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.model.User;
import com.project.finsync.repository.BudgetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public Iterable<Budget> findAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget findBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found with id: " + budgetId));
    }

    public List<Budget> findBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public List<Budget> findBudgetsByUserByMonth(User user, Month month) {
        return budgetRepository.findByUserAndMonth(user, month);
    }

    public List<Budget> findBudgetByUserByCategory(User user, ExpenseCategory category) {
        return budgetRepository.findByUserAndCategory(user, category);
    }

    public Double findTotalAmountForBudgets(User user) {
        return budgetRepository.findByUser(user)
                .stream()
                .mapToDouble(Budget::getAmount)
                .sum();
    }

    public Budget createBudget(User user, Budget budget) {
        budget.setUser(user);
        return budgetRepository.save(budget);
    }

    public Optional<Budget> updateBudget(Long budgetId, Budget updateBudget) {
        return budgetRepository.findById(budgetId).map(budget -> {
            if (updateBudget.getAmount() != null) {
                budget.setAmount(updateBudget.getAmount());
            }
            return budgetRepository.save(budget);
        });
    }

    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

}
