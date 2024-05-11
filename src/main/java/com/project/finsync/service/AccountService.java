package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"accounts"})
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Cacheable(key = "#userId")
    public List<Account> findAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Cacheable(key = "#id + #userId")
    public Optional<Account> findAccountByIdAndUser(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId);
    }

    public Optional<Account> createAccount(Long userId, Account account) {
        return userService.findUserById(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        });
    }

    @CachePut(key = "#id + #userId")
    public Optional<Account> updateAccount(Long id, Long userId, Account updateAccount) {
        return findAccountByIdAndUser(id, userId).map(account -> {
            if (updateAccount.getAccountNo() != null) {
                account.setAccountNo(updateAccount.getAccountNo());
            }
            if (updateAccount.getType() != null) {
                account.setType(updateAccount.getType());
            }
            if (updateAccount.getCurrency() != null) {
                account.setCurrency(updateAccount.getCurrency());
            }
            return accountRepository.save(account);
        });
    }

    @CacheEvict(key = "#userId")
    public void deleteAllAccountsByUserId(Long userId) {
        accountRepository.deleteAllByUserId(userId);
    }

    @CacheEvict(key = "#id + #userId")
    public void deleteAccountByIdAndUserId(Long id, Long userId) {
        findAccountByIdAndUser(id, userId).ifPresent(account ->
                accountRepository.deleteById(account.getId())
        );
    }
}
