package com.project.finsync.controller;

import com.project.finsync.model.Account;
import com.project.finsync.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public Iterable<Account> findByUserId(@PathVariable Long userId) {
        return accountService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        Optional<Account> account = accountService.findByIdAndUserId(id, userId);
        return account.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccountByUserId(@PathVariable Long userId, @Validated @RequestBody Account account) {
        Account createdAccount = accountService.createAccountByUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    ResponseEntity<Account> updateAccountByUserId(@PathVariable Long id, @Validated @RequestBody Account newAccount) {
        Optional<Account> updatedAccount = accountService.updateAccountByUserId(id, newAccount);
        return updatedAccount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public void deleteAllUserAccounts(@PathVariable Long userId) {
        accountService.deleteAllUserAccounts(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        accountService.deleteByIdAndUserId(id, userId);
    }
}
