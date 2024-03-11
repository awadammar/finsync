package com.project.finsync.repository;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Account;
import com.project.finsync.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);

    List<Transaction> findByAccountAndType(Account account, TransactionType type);

    List<Transaction> findByAccountAndCategory(Account account, ExpenseCategory category);

    List<Transaction> findByAccountAndTagsIn(Account account, Set<String> tags);
}
