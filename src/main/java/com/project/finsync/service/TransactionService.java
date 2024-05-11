package com.project.finsync.service;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Transaction;
import com.project.finsync.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"transactions"})
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Cacheable(key = "#accountId")
    public List<Transaction> findTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Cacheable(key = "#transactionId + #accountId")
    public Optional<Transaction> findTransactionByIdAndAccount(Long transactionId, Long accountId) {
        return transactionRepository.findByIdAndAccountId(transactionId, accountId);
    }

    public List<Transaction> findTransactionsByAccountByMonth(Long accountId, Month month) {
        return findTransactionsByAccount(accountId)
                .stream()
                .filter(transaction -> month.equals(transaction.getDate().getMonth()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByType(Long accountId, TransactionType transactionType) {
        return findTransactionsByAccount(accountId)
                .stream()
                .filter(transaction -> transactionType.equals(transaction.getType()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByCategory(Long accountId, ExpenseCategory expenseCategory) {
        return findTransactionsByAccount(accountId)
                .stream()
                .filter(transaction -> expenseCategory.equals(transaction.getCategory()))
                .toList();
    }

    public List<Transaction> findTransactionsByAccountByTags(Long accountId, Set<String> tags) {
        return findTransactionsByAccount(accountId)
                .stream()
                .filter(transaction -> !Collections.disjoint(transaction.getTags(), tags))
                .toList();
    }

    public Double getTotalAmountByAccount(Long accountId) {
        return findTransactionsByAccount(accountId)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Optional<Transaction> createTransaction(Long accountId, Transaction transaction) {
        return accountService.findAccountById(accountId).map(account -> {
            transaction.setAccount(account);
            return transactionRepository.save(transaction);
        });
    }

    @CachePut(key = "#transactionId + #accountId")
    public Optional<Transaction> updateTransaction(Long transactionId, Long accountId, Transaction updateTransaction) {
        return findTransactionByIdAndAccount(transactionId, accountId).map(budget -> {
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

    @CacheEvict(key = "#accountId")
    public void deleteAllTransactionsByAccount(Long accountId) {
        List<Long> ids = transactionRepository.findByAccountId(accountId)
                .stream()
                .map(Transaction::getId)
                .toList();
        transactionRepository.deleteAllById(ids);
    }

    @CacheEvict(key = "#transactionId + #accountId")
    public void deleteTransaction(Long transactionId, Long accountId) {
        findTransactionByIdAndAccount(transactionId, accountId).ifPresent(transaction -> transactionRepository.deleteById(transactionId));
    }
}
