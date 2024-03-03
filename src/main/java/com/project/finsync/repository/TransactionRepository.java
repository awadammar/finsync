package com.project.finsync.repository;

import com.project.finsync.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
