package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Iterable<Account> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> findByIdAndUserId(Long id, Long userId) {
        return accountRepository.findByIdAndUserId(id, userId);
    }

    public Account createAccountByUserId(Long userId) {
        Account account = new Account(userId);
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccountByUserId(Long id, Account newAccount) {
        return accountRepository.findById(id).map(account -> {
            if (newAccount.getAccountName() != null) {
                account.setAccountName(newAccount.getAccountName());
            }
            if (newAccount.getAccountType() != null) {
                account.setAccountType(newAccount.getAccountType());
            }
            if (newAccount.getCurrency() != null) {
                account.setCurrency(newAccount.getCurrency());
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
