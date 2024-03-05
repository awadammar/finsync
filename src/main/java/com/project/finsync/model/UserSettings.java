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
    @OneToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    private Boolean pushNotification;
    private Boolean deleteAccount;

    public UserSettings(Long userId) {
        this.userId = userId;
        pushNotification = true;
        deleteAccount = false;
    }
}
