package com.project.finsync.service;

import com.project.finsync.model.User;
import com.project.finsync.model.UserSettings;
import com.project.finsync.repository.AccountRepository;
import com.project.finsync.repository.UserRepository;
import com.project.finsync.repository.UserSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"users"})
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserSettingsRepository userSettingsRepository;

    public List<User> findAllUsers() {
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return allUsers;
    }

    @Cacheable(key = "#id")
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) throws IllegalArgumentException {
        User savedUser = userRepository.save(user);
        savedUser.setPassword(user.getPassword());

        UserSettings userSettings = new UserSettings(user);
        userSettingsRepository.save(userSettings);

        return savedUser;
    }

    @CachePut(key = "#updatedUser.id")
    public Optional<User> updateUser(User updatedUser) {
        return findUserById(updatedUser.getId())
                .map(o -> userRepository.save(updatedUser));
    }

    @Transactional
    @CacheEvict(key = "#userId")
    public void deleteUser(Long id) {
        findUserById(id).ifPresent(user -> {
            accountRepository.deleteAllByUserId(user.getId());
            userSettingsRepository.deleteByUserId(user.getId());
            userRepository.delete(user);
        });
    }
}
