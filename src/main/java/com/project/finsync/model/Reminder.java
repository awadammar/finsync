package com.project.finsync.model;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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
    private ReminderType type;
    private LocalDate date;
    private String description;
    @Enumerated(EnumType.STRING)
    private ReminderStatus status;
}
