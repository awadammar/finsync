package com.project.finsync.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Example:
    // currencyPreference;
    // notificationEnabled;
}
