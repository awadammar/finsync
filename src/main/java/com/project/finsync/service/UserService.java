package com.project.finsync.service;

import com.project.finsync.model.User;
import com.project.finsync.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;
    private final GoalRepository goalRepository;
    private final ReminderRepository reminderRepository;
    private final TransactionRepository transactionRepository;
    private final UserSettingsRepository userSettingsRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        User newUser = userRepository.save(user);
        accountRepository.createAccountByUserId(user.getId());
        userSettingsRepository.createSettingsByUserId(user.getId());
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
//            budgetRepository.deleteByUserId(id);
//            goalRepository.deleteByUserId(id);
//            reminderRepository.deleteByUserId(id);
//            transactionRepository.deleteByUserId(id);
            userSettingsRepository.deleteByUserId(id);
            userRepository.delete(user);
        });
    }
}
