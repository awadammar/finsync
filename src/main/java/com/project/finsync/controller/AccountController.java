package com.project.finsync.controller;

import com.project.finsync.model.Account;
import com.project.finsync.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public Iterable<Account> findAllAccountsForUser(@PathVariable Long userId) {
        return accountService.findAccountsByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findAccountByIdForUser(@PathVariable Long id, @PathVariable Long userId) {
        return accountService.findAccountByIdAndUser(id, userId)
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@PathVariable Long userId, @Valid @RequestBody Account account) {
        return accountService.createAccount(userId, account)
                .map(createdAccount -> ResponseEntity.status(HttpStatus.CREATED).body(createdAccount))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Account> updateAccount(@PathVariable Long id, @PathVariable Long userId, @Valid @RequestBody Account newAccount) {
        return accountService.updateAccount(id, userId, newAccount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAccountsForUser(@PathVariable Long userId) {
        accountService.deleteAllAccountsByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountByIdForUser(@PathVariable Long id, @PathVariable Long userId) {
        accountService.deleteAccountByIdAndUserId(id, userId);
        return ResponseEntity.noContent().build();
    }
}
