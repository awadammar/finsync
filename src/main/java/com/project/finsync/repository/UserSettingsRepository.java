package com.project.finsync.repository;

import com.project.finsync.model.UserSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSettingsRepository extends CrudRepository<UserSettings, Long> {
    Optional<UserSettings> findByUserId(Long userId);

    void deleteByUserId(Long userId);

}
