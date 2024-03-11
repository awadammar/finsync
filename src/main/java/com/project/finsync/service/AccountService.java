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

    public Iterable<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> findByIdAndUserId(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId);
    }

    public Optional<Account> createAccount(Long userId, Account account) {
        return userRepository.findById(userId).map(user -> {
            account.setUser(user);
            return accountRepository.save(account);
        });
    }

    public Optional<Account> updateAccount(Long id, Account updateAccount) {
        return accountRepository.findById(id).map(account -> {
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

    public void deleteAllUserAccounts(Long userId) {
        List<Long> accountIds = accountRepository.findByUserId(userId)
                .stream()
                .map(Account::getId)
                .toList();
        accountRepository.deleteAllById(accountIds);
    }

    public void deleteByIdAndUserId(Long id, Long userId) {
        findByIdAndUserId(id, userId).ifPresent(account ->
                accountRepository.deleteById(account.getId())
        );
    }
}
