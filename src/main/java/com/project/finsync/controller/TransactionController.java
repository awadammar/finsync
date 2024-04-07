package com.project.finsync.controller;

import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Transaction;
import com.project.finsync.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/accounts/{accountId}/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> findTransactionByIdForAccount(@PathVariable Long transactionId, @PathVariable Long accountId) {
        Optional<Transaction> transaction = transactionService.findTransactionByIdAndAccount(transactionId, accountId);
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Transaction> findTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.findTransactionsByAccount(accountId);
    }

    @GetMapping("/month/{month}")
    public List<Transaction> findTransactionsByAccountByMonth(@PathVariable Long accountId, @PathVariable Month month) {
        return transactionService.findTransactionsByAccountByMonth(accountId, month);
    }

    @GetMapping("/type/{transactionType}")
    public List<Transaction> findTransactionsByAccountByType(@PathVariable Long accountId, @PathVariable TransactionType transactionType) {
        return transactionService.findTransactionsByAccountByType(accountId, transactionType);
    }

    @GetMapping("/category/{expenseCategory}")
    public List<Transaction> findTransactionsByAccountByCategory(@PathVariable Long accountId, @PathVariable ExpenseCategory expenseCategory) {
        return transactionService.findTransactionsByAccountByCategory(accountId, expenseCategory);
    }

    @GetMapping("/tags")
    public List<Transaction> findTransactionsByAccountByTags(@PathVariable Long accountId, @RequestParam Set<String> tags) {
        return transactionService.findTransactionsByAccountByTags(accountId, tags);
    }

    @GetMapping("/total-amount")
    public Double getTotalAmountByAccount(@PathVariable Long accountId) {
        return transactionService.getTotalAmountByAccount(accountId);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@PathVariable Long accountId, @RequestBody Transaction transaction) {
        Optional<Transaction> createdTransaction = transactionService.createTransaction(accountId, transaction);
        return createdTransaction.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long transactionId, @PathVariable Long accountId, @RequestBody Transaction transaction) {
        Optional<Transaction> updatedTransaction = transactionService.updateTransaction(transactionId, accountId, transaction);
        return updatedTransaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransactionsForAccount(@PathVariable Long accountId) {
        transactionService.deleteAllTransactionsByAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId, @PathVariable Long accountId) {
        transactionService.deleteTransaction(transactionId, accountId);
        return ResponseEntity.noContent().build();
    }

}
