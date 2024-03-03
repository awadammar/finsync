package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.repository.AccountRepository;
import com.project.finsync.util.UserUtils;
import com.project.finsync.util.exceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Account createAccount(Account account) {
        account.setUserId(account.getUserId());
        return accountRepository.save(account);
    }

    public Optional<Account> updateAccount(Long id, Account newAccount) {
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
        for (Account account : accountRepository.findByUserId(userId)) {
            if(UserUtils.belongsToUser(account, userId)) {
                accountRepository.deleteById(account.getId());
            }
        }
    }

    public void deleteByIdAndUserId(Long id, Long userId) {
        findByIdAndUserId(id, userId).ifPresent(account -> {
            if (UserUtils.belongsToUser(account, userId)) {
                accountRepository.deleteById(account.getId());
            } else {
                throw new UnauthorizedAccessException("User is not authorized to delete this account");
            }
        });
    }
}
