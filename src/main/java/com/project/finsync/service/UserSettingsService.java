package com.project.finsync.service;

import com.project.finsync.model.UserSettings;
import com.project.finsync.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;

    public Optional<UserSettings> findSettingsByUserId(Long userId) {
        return userSettingsRepository.findByUserId(userId);
    }

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

    public void deleteByUserId(Long userId) {
        findSettingsByUserId(userId).ifPresent(userSetting ->
                userSettingsRepository.deleteByUserId(userSetting.getUser().getId())
        );
    }

}
