package com.project.finsync.service;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.repository.ReminderRepository;
import com.project.finsync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public Iterable<Reminder> findAllReminders() {
        return reminderRepository.findAll();
    }

    public Reminder findReminderById(Long reminderId) {
        return reminderRepository.findById(reminderId)
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found with id: " + reminderId));
    }

    public List<Reminder> findRemindersByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(reminderRepository::findByUser)
                .orElse(Collections.emptyList());
    }

    public List<Reminder> findRemindersByUserByStatus(Long userId, ReminderStatus status) {
        return findRemindersByUserId(userId)
                .stream()
                .filter(reminder -> status.equals(reminder.getStatus()))
                .toList();
    }

    public List<Reminder> findRemindersByUserByDate(Long userId, LocalDate date) {
        return findRemindersByUserId(userId)
                .stream()
                .filter(reminder -> date.isEqual(reminder.getDueDate()))
                .toList();
    }

    public Optional<Reminder> createReminder(Long userId, Reminder reminder) {
        return userRepository.findById(userId).map(user -> {
            reminder.setUser(user);
            reminder.setStatus(ReminderStatus.PENDING);
            return reminderRepository.save(reminder);
        });
    }

    public Optional<Reminder> updateReminder(Long reminderId, Reminder updateReminder) {
        return reminderRepository.findById(reminderId).map(reminder -> {
            if (updateReminder.getStatus() != null) {
                reminder.setStatus(updateReminder.getStatus());
            }
            if (updateReminder.getDueAmount() != null) {
                reminder.setDueAmount(updateReminder.getDueAmount());
            }
            if (updateReminder.getDueDate() != null) {
                reminder.setDueDate(updateReminder.getDueDate());
            }
            if (updateReminder.getDescription() != null) {
                reminder.setDescription(updateReminder.getDescription());
            }
            if (updateReminder.getType() != null) {
                reminder.setType(updateReminder.getType());
            }
            return reminderRepository.save(reminder);
        });
    }

    public void markReminderAsComplete(Long reminderId) {
        reminderRepository.findById(reminderId)
                .ifPresent(reminder -> reminder.setStatus(ReminderStatus.COMPLETED));
    }

    public void deleteAllUserReminders(Long userId) {
        findRemindersByUserId(userId)
                .forEach(reminder -> deleteReminder(reminder.getReminderId()));
    }

    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

}
