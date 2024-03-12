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
        return accountService.findAccountByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        Optional<Account> account = accountService.findAccountByIdAndUserId(id, userId);
        return account.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@PathVariable Long userId, @Validated @RequestBody Account account) {
        return accountService.createAccount(userId, account)
                .map(createdAccount -> ResponseEntity.status(HttpStatus.CREATED).body(createdAccount))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Account> updateAccount(@PathVariable Long id, @Validated @RequestBody Account newAccount) {
        Optional<Account> updatedAccount = accountService.updateAccount(id, newAccount);
        return updatedAccount.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUserAccounts(@PathVariable Long userId) {
        accountService.deleteAllUserAccounts(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByIdAndUserId(@PathVariable Long id, @PathVariable Long userId) {
        accountService.deleteByIdAndUserId(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
