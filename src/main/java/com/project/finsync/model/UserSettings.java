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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean pushNotification;
    private Boolean deleteAccount;

    public UserSettings() {
        this.pushNotification = true;
        this.deleteAccount = false;
    }

    public UserSettings(User user) {
        this.user = user;
        this.pushNotification = true;
        this.deleteAccount = false;
    }
}
