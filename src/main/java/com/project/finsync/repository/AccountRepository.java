package com.project.finsync.repository;

import com.project.finsync.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    Account createAccount(Account account);
    void deleteAllUserAccounts(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

}
