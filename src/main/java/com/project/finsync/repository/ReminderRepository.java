package com.project.finsync.repository;

import com.project.finsync.model.Reminder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {
    List<Reminder> findByUserId(Long userId);

    Optional<Reminder> findByIdAndUserId(Long id, Long userId);

}
