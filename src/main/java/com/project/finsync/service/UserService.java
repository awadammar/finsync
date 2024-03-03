package com.project.finsync.service;

import com.project.finsync.enums.AccountType;
import com.project.finsync.model.Account;
import com.project.finsync.model.User;
import com.project.finsync.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.finsync.util.UserUtils.ACCOUNT_DEFAULT_NAME;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User newUser = userRepository.save(user);

        // Create an account for the new user
        Account account = new Account();
        account.setUserId(newUser.getId());
        account.setAccountType(AccountType.PERSONAL);
        account.setAccountName(ACCOUNT_DEFAULT_NAME);
        accountRepository.createAccount(account);
        return newUser;
    }

    public Optional<User> updateUser(Long id, User newUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(newUser.getUsername());
            if (newUser.getEmail() != null) {
                user.setEmail(newUser.getEmail());
            }
            if (newUser.getUsername() != null) {
                user.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null) {
                user.setPassword(newUser.getPassword());
            }
            return userRepository.save(user);
        });
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            accountRepository.deleteAllUserAccounts(id);
            userRepository.delete(user);
        });
    }
}
