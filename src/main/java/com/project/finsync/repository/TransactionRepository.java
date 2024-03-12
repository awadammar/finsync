package com.project.finsync.repository;

import com.project.finsync.model.Account;
import com.project.finsync.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);
}
