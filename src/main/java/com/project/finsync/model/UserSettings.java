package com.project.finsync.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "push_notification")
    private Boolean pushNotification;

    @Column(name = "delete_account")
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
