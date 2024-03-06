package com.project.finsync.controller;

import com.project.finsync.model.UserSettings;
import com.project.finsync.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserSettings> findByUserId(@PathVariable Long userId) {
        Optional<UserSettings> settings = userSettingsService.findByUserId(userId);
        return settings.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    ResponseEntity<UserSettings> updateSettings(@PathVariable Long userId, @Validated @RequestBody UserSettings newUserSettings) {
        Optional<UserSettings> updatedUserSettings = userSettingsService.updateUserSettings(userId, newUserSettings);
        return updatedUserSettings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
        userSettingsService.deleteByUserId(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
