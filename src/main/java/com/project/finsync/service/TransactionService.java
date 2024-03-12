package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Transaction;
import com.project.finsync.repository.AccountRepository;
import com.project.finsync.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public Iterable<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));
    }

    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        return accountRepository.findById(accountId)
                .map(transactionRepository::findByAccount)
                .orElse(Collections.emptyList());
    }

    public List<Transaction> findTransactionsByAccountByMonth(Long accountId, Month month) {
        return findTransactionsByAccountId(accountId)
                .stream()
                .filter(transaction -> month.equals(transaction.getDate().getMonth()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByType(Long accountId, TransactionType transactionType) {
        return findTransactionsByAccountId(accountId)
                .stream()
                .filter(transaction -> transactionType.equals(transaction.getType()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByCategory(Long accountId, ExpenseCategory expenseCategory) {
        return findTransactionsByAccountId(accountId)
                .stream()
                .filter(transaction -> expenseCategory.equals(transaction.getCategory()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByTags(Long accountId, Set<String> tags) {
        return findTransactionsByAccountId(accountId)
                .stream()
                .filter(transaction -> !Collections.disjoint(transaction.getTags(), tags))
                .toList();
    }

    public Double getTotalAmountByAccount(Long accountId) {
        return findTransactionsByAccountId(accountId)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Optional<Transaction> createTransaction(Long accountId, Transaction transaction) {
        return accountRepository.findById(accountId).map(account -> {
            transaction.setAccount(account);
            return transactionRepository.save(transaction);
        });
    }

    public Optional<Transaction> updateTransaction(Long transactionId, Transaction updateTransaction) {
        return transactionRepository.findById(transactionId).map(budget -> {
            if (updateTransaction.getAmount() != null) {
                budget.setAmount(updateTransaction.getAmount());
            }
            if (updateTransaction.getAccount() != null) {
                budget.setAccount(updateTransaction.getAccount());
            }
            if (updateTransaction.getDate() != null) {
                budget.setDate(updateTransaction.getDate());
            }
            if (updateTransaction.getDescription() != null) {
                budget.setDescription(updateTransaction.getDescription());
            }
            if (updateTransaction.getCategory() != null) {
                budget.setCategory(updateTransaction.getCategory());
            }
            if (updateTransaction.getLocation() != null) {
                budget.setLocation(updateTransaction.getLocation());
            }
            if (!CollectionUtils.isEmpty(updateTransaction.getTags())) {
                budget.getTags().addAll(updateTransaction.getTags());
            }
            return transactionRepository.save(budget);
        });
    }

    public void deleteAccountTransactions(Long accountId) {
        findTransactionsByAccountId(accountId)
                .forEach(reminder -> deleteTransaction(reminder.getTransactionId()));
    }

    public void deleteTransaction(Long budgetId) {
        transactionRepository.deleteById(budgetId);
    }
}
