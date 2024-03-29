package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.repository.AccountRepository;
import com.project.finsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Iterable<Account> findAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> findAccountByIdAndUser(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId);
    }

    public Optional<Account> createAccount(Long userId, Account account) {
        return userRepository.findById(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        });
    }

    public Optional<Account> updateAccount(Long id, Long userId, Account updateAccount) {
        return findAccountByIdAndUser(id, userId).map(account -> {
            if (updateAccount.getName() != null) {
                account.setName(updateAccount.getName());
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

    public void deleteAllAccountsByUserId(Long userId) {
        List<Long> ids = accountRepository.findByUserId(userId)
                .stream()
                .map(Account::getId)
                .toList();
        accountRepository.deleteAllById(ids);
    }

    public void deleteAccountByIdAndUserId(Long id, Long userId) {
        findAccountByIdAndUser(id, userId).ifPresent(account ->
                accountRepository.deleteById(account.getId())
        );
    }
}
