package com.project.finsync.model;

import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.project.finsync.enums.ReminderStatus.ACTIVE;

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

    public Reminder(User user, ReminderType type, String description) {
        this.user = user;
        this.type = type;
        this.description = description;
        this.status = ACTIVE;
    }

}
