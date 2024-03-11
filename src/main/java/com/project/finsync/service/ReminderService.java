package com.project.finsync.service;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.model.User;
import com.project.finsync.repository.ReminderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public Iterable<Reminder> findAllReminders() {
        return reminderRepository.findAll();
    }

    public Reminder findReminderById(Long reminderId) {
        return reminderRepository.findById(reminderId)
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found with id: " + reminderId));
    }

    public List<Reminder> findRemindersByUser(User user) {
        return reminderRepository.findByUser(user);
    }

    public List<Reminder> findRemindersByUserByStatus(User user, ReminderStatus status) {
        return reminderRepository.findByUser(user)
                .stream()
                .filter(reminder -> reminder.getStatus().equals(status))
                .toList();
    }

    public List<Reminder> findRemindersByUserByDate(User user, LocalDate date) {
        return reminderRepository.findByUser(user)
                .stream()
                .filter(reminder -> reminder.getDueDate().isEqual(date))
                .toList();
    }

    public Reminder createReminder(User user, Reminder reminder) {
        reminder.setUser(user);
        reminder.setStatus(ReminderStatus.PENDING);
        return reminderRepository.save(reminder);
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

    public void deleteAllUserReminders(User user) {
        reminderRepository.findByUser(user)
                .forEach(reminder -> deleteReminder(reminder.getReminderId()));
    }

    public void deleteReminder(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

}
