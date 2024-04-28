package com.project.finsync.service;

import com.project.finsync.model.UserSettings;
import com.project.finsync.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"user_settings"})
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;

    @Cacheable(key = "#userId")
    public Optional<UserSettings> findSettingsByUserId(Long userId) {
        return userSettingsRepository.findByUserId(userId);
    }

    @CachePut(key = "#userId")
    public Optional<UserSettings> updateSettings(Long userId, UserSettings updatedUserSettings) {
        return findSettingsByUserId(userId).map(userSettings -> {
            if (updatedUserSettings.getPushNotification() != null) {
                userSettings.setPushNotification(updatedUserSettings.getPushNotification());
            }
            if (updatedUserSettings.getDeleteAccount() != null) {
                userSettings.setDeleteAccount(updatedUserSettings.getDeleteAccount());
            }
            return userSettingsRepository.save(userSettings);
        });
    }

    @CacheEvict(key = "#userId")
    public void deleteByUserId(Long userId) {
        findSettingsByUserId(userId).ifPresent(userSetting ->
                userSettingsRepository.deleteByUserId(userSetting.getUser().getId())
        );
    }

}
