package com.project.finsync.domain.model.repository;

import com.project.finsync.domain.model.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
