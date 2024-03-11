package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Account;
import com.project.finsync.model.Transaction;
import com.project.finsync.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));
    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> getTransactionsByAccountByMonth(Account account, Month month) {
        return transactionRepository.findByAccount(account)
                .stream()
                .filter(transaction -> month.equals(transaction.getDate().getMonth()))
                .toList();
    }

    public List<Transaction> getTransactionsByAccountByType(Account account, TransactionType transactionType) {
        return transactionRepository.findByAccountAndType(account, transactionType);
    }

    public List<Transaction> getTransactionsByAccountByCategory(Account account, ExpenseCategory expenseCategory) {
        return transactionRepository.findByAccountAndCategory(account, expenseCategory);
    }

    public List<Transaction> getTransactionsByAccountByCategory(Account account, Set<String> tags) {
        return transactionRepository.findByAccountAndTagsIn(account, tags);
    }

    public Double getTotalAmountByAccount(Account account) {
        return transactionRepository.findByAccount(account)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
