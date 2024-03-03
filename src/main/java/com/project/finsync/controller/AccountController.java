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
    public ResponseEntity<Account> createAccount(@PathVariable Long userId, @Validated @RequestBody Account account) {
        Account createdAccount = accountService.createAccount(userId, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id, @PathVariable Long userId) {
        accountService.deleteByIdAndUserId(id, userId);
    }
}
