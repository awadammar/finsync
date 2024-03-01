package com.project.finsync.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String goalName;
    private double goalAmount;
    private LocalDate targetDate;
    private double currentProgress;
    private String status;
}
