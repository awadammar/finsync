package com.project.finsync.model;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;
    private LocalDate reminderDate;
    private String description;
    @Enumerated(EnumType.STRING)
    private ReminderStatus status;
}
