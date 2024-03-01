package com.project.finsync.domain.model.repository;

import com.project.finsync.domain.model.entity.Budget;
import org.springframework.data.repository.CrudRepository;

public interface BudgetRepository extends CrudRepository<Budget, Long> {

}
