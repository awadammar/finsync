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

    public Optional<UserSettings> findByUserId(Long userId) {
        return userSettingsRepository.findByUserId(userId);
    }

    public UserSettings createSettingsByUserId(Long userId) {
        UserSettings userSettings = new UserSettings(userId);
        return userSettingsRepository.save(userSettings);
    }

    public Optional<UserSettings> updateSettingsByUserId(Long id, UserSettings newUserSettings) {
        return userSettingsRepository.findById(id).map(account -> {
            if (newUserSettings.getPushNotification() != null) {
                account.setPushNotification(newUserSettings.getPushNotification());
            }
            if (newUserSettings.getDeleteAccount() != null) {
                account.setDeleteAccount(newUserSettings.getDeleteAccount());
            }
            return userSettingsRepository.save(account);
        });
    }

    public void deleteByUserId(Long userId) {
        findByUserId(userId).ifPresent(userSetting ->
                userSettingsRepository.deleteById(userSetting.getUserId())
        );
    }

}
