package com.project.finsync.repository;

import com.project.finsync.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    Optional<Transaction> findByIdAndAccountId(Long id, Long userId);

}
