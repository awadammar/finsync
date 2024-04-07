package com.project.finsync.controller;

import com.project.finsync.model.UserSettings;
import com.project.finsync.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/settings")
@RequiredArgsConstructor
public class UserSettingsController {
    private final UserSettingsService userSettingsService;

    @GetMapping
    public ResponseEntity<UserSettings> findSettingsForUser(@PathVariable Long userId) {
        Optional<UserSettings> settings = userSettingsService.findSettingsByUserId(userId);
        return settings.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    ResponseEntity<UserSettings> updateSettings(@PathVariable Long userId, @Validated @RequestBody UserSettings newUserSettings) {
        Optional<UserSettings> updatedUserSettings = userSettingsService.updateSettings(userId, newUserSettings);
        return updatedUserSettings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSettingsForUser(@PathVariable Long userId) {
        userSettingsService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
