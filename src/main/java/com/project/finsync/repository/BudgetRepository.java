package com.project.finsync.repository;

import com.project.finsync.model.Budget;
import com.project.finsync.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
    List<Budget> findByUser(User user);

}
