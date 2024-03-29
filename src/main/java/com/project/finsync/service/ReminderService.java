package com.project.finsync.service;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.repository.ReminderRepository;
import com.project.finsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    public Optional<Reminder> findReminderByIdAndUser(Long reminderId, Long userId) {
        return reminderRepository.findByReminderIdAndUserId(reminderId, userId);
    }

    public List<Reminder> findRemindersByUser(Long userId) {
        return reminderRepository.findByUserId(userId);
    }

    public List<Reminder> findRemindersByUserByStatus(Long userId, ReminderStatus status) {
        return findRemindersByUser(userId)
                .stream()
                .filter(reminder -> status.equals(reminder.getStatus()))
                .toList();
    }

    public List<Reminder> findRemindersByUserByDate(Long userId, LocalDate date) {
        return findRemindersByUser(userId)
                .stream()
                .filter(reminder -> date.isEqual(reminder.getDueDate()))
                .toList();
    }

    public Optional<Reminder> createReminder(Long userId, Reminder reminder) {
        return userRepository.findById(userId).map(user -> {
            reminder.setUser(user);
            return reminderRepository.save(reminder);
        });
    }

    public void markReminderAsComplete(Long reminderId) {
        reminderRepository.findById(reminderId)
                .ifPresent(reminder -> reminder.setStatus(ReminderStatus.COMPLETED));
    }

    public Optional<Reminder> updateReminder(Long reminderId, Long userId, Reminder updateReminder) {
        return findReminderByIdAndUser(reminderId, userId).map(reminder -> {
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

    public void deleteAllRemindersByUser(Long userId) {
        List<Long> ids = reminderRepository.findByUserId(userId)
                .stream()
                .map(Reminder::getReminderId)
                .toList();
        reminderRepository.deleteAllById(ids);

        findRemindersByUser(userId)
                .forEach(reminder -> deleteReminder(reminder.getReminderId(), userId));
    }

    public void deleteReminder(Long reminderId, Long userId) {
        findReminderByIdAndUser(reminderId, userId).ifPresent(reminder -> reminderRepository.deleteById(reminderId));
    }

}
