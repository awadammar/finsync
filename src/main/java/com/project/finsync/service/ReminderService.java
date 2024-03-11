package com.project.finsync.service;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.model.User;
import com.project.finsync.repository.ReminderRepository;
import com.project.finsync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public List<Reminder> findRemindersByUser(User user) {
        return reminderRepository.findByUser(user);
    }

    public List<Reminder> findRemindersByUserByStatus(User user, ReminderStatus status) {
        return reminderRepository.findByUser(user)
                .stream()
                .filter(reminder -> reminder.getStatus().equals(status))
                .toList();
    }

    public void markReminderAsComplete(Long reminderId) {
        reminderRepository.findById(reminderId)
                .ifPresent(reminder -> reminder.setStatus(ReminderStatus.COMPLETED));
    }

    public List<Reminder> findRemindersByUserByDate(User user, LocalDate date) {
        return reminderRepository.findByUser(user)
                .stream()
                .filter(reminder -> reminder.getDate().isEqual(date))
                .toList();
    }

}
