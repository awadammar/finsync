package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Account;
import com.project.finsync.model.Transaction;
import com.project.finsync.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Iterable<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));
    }

    public List<Transaction> findTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> findTransactionsByAccountByMonth(Account account, Month month) {
        return transactionRepository.findByAccount(account)
                .stream()
                .filter(transaction -> month.equals(transaction.getDate().getMonth()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByType(Account account, TransactionType transactionType) {
        return transactionRepository.findByAccountAndType(account, transactionType);
    }

    public List<Transaction> findTransactionsByAccountByCategory(Account account, ExpenseCategory expenseCategory) {
        return transactionRepository.findByAccountAndCategory(account, expenseCategory);
    }

    public List<Transaction> findTransactionsByAccountByTags(Account account, Set<String> tags) {
        return transactionRepository.findByAccountAndTagsIn(account, tags);
    }

    public Double getTotalAmountByAccount(Account account) {
        return transactionRepository.findByAccount(account)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Transaction createTransaction(Account account, Transaction transaction) {
        transaction.setAccount(account);
        return transactionRepository.save(transaction);
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

    public void deleteAccountTransactions(Account account) {
        transactionRepository.findByAccount(account)
                .forEach(reminder -> deleteTransaction(reminder.getTransactionId()));
    }

    public void deleteTransaction(Long budgetId) {
        transactionRepository.deleteById(budgetId);
    }
}
