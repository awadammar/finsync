package com.project.finsync.utils;

import com.project.finsync.model.User;

public class TestUtils {
    public static User createSimpleUser() {
        User user = new User("test@example.com", "password");
        user.setId(1L);
        return user;
    }
}
