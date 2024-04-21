package com.project.finsync.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.enums.ReminderType;
import com.project.finsync.validation.EnumValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.project.finsync.enums.ReminderStatus.ACTIVE;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = ReminderType.class)
    private ReminderType type;

    @Positive(message = "Due Amount amount must be a positive value")
    @Column(name = "due_amount")
    private Double dueAmount;

    @Future(message = "Due date must be after than today")
    @Column(name = "due_date")
    private LocalDate dueDate;

    private String description;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @EnumValidator(enumClazz = ReminderStatus.class)
    private ReminderStatus status;

    public Reminder(User user, ReminderType type, String description) {
        this.user = user;
        this.type = type;
        this.description = description;
        this.status = ACTIVE;
    }

}
