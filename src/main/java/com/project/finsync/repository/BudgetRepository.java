package com.project.finsync.repository;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.model.User;
import org.springframework.data.repository.CrudRepository;

import java.time.Month;
import java.util.List;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
    List<Budget> findByUser(User user);

    List<Budget> findByUserAndMonth(User user, Month month);

    List<Budget> findByUserAndCategory(User user, ExpenseCategory category);

}
