package com.project.finsync.service;

import com.project.finsync.model.Account;
import com.project.finsync.model.User;
import com.project.finsync.model.UserSettings;
import com.project.finsync.repository.UserRepository;
import com.project.finsync.repository.UserSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final UserSettingsRepository userSettingsRepository;

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        savedUser.setUsername(user.getUsername());
        savedUser.setPassword(user.getPassword());

        UserSettings userSettings = new UserSettings(user);
        userSettingsRepository.save(userSettings);

        Account account = new Account(user);
        accountService.createAccount(user.getId(), account);

        return savedUser;
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                throw new UnsupportedOperationException("Cannot update email field");
            }
            if (updatedUser.getPassword() != null) {
                user.setPassword(updatedUser.getPassword());
            }
            return userRepository.save(user);
        });
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            accountService.deleteAllUserAccounts(user.getId());
            userSettingsRepository.deleteByUserId(user.getId());
            userRepository.delete(user);
        });
    }
}
