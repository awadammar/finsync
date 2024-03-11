package com.project.finsync.model;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.project.finsync.enums.ReminderStatus.PENDING;

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
    private ReminderType type;
    private Double dueAmount;
    private LocalDate dueDate;
    private String description;
    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    public Reminder(ReminderType type) {
        this.type = type;
        this.description = "";
        this.status = PENDING;
    }
}
