package com.project.finsync.repository;

import com.project.finsync.model.Budget;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByBudgetIdAndUserId(Long id, Long userId);

}
