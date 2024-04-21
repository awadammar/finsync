package com.project.finsync.controller;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/users/{userId}/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping
    public Iterable<Reminder> findAllRemindersForUser(@PathVariable Long userId) {
        return reminderService.findRemindersByUser(userId);
    }

    @GetMapping("/status/{status}")
    public Iterable<Reminder> findRemindersByStatusForUser(@PathVariable Long userId, @PathVariable ReminderStatus status) {
        return reminderService.findRemindersByUserByStatus(userId, status);
    }

    @GetMapping("/date/{date}")
    public Iterable<Reminder> findRemindersByDateForUser(@PathVariable Long userId, @PathVariable LocalDate date) {
        return reminderService.findRemindersByUserByDate(userId, date);
    }

    @PostMapping
    public ResponseEntity<Reminder> createReminderForUser(@PathVariable Long userId, @Valid @RequestBody Reminder reminder) {
        return reminderService.createReminder(userId, reminder)
                .map(createdReminder -> ResponseEntity.status(HttpStatus.CREATED).body(createdReminder))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{reminderId}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable Long reminderId, @PathVariable Long userId, @Valid @RequestBody Reminder reminder) {
        return reminderService.updateReminder(reminderId, userId, reminder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{reminderId}/complete")
    public ResponseEntity<Void> markReminderAsComplete(@PathVariable Long reminderId) {
        reminderService.markReminderAsComplete(reminderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllRemindersForUser(@PathVariable Long userId) {
        reminderService.deleteAllRemindersByUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long reminderId, @PathVariable Long userId) {
        reminderService.deleteReminder(reminderId, userId);
        return ResponseEntity.noContent().build();
    }
}
