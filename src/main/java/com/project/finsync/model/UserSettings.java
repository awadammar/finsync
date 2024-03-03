package com.project.finsync.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "settings")
public class UserSettings {
    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    // Example:
    // currencyPreference;
    // notificationEnabled;
}
