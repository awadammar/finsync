package com.project.finsync;

import com.project.finsync.model.User;

public class TestUtils {
    public static User createBasicUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        return user;
    }
}
