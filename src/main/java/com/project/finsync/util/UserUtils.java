package com.project.finsync.util;

import com.project.finsync.model.Account;

public class UserUtils {
    public static final String ACCOUNT_DEFAULT_NAME = "Default Account";
    private UserUtils() {}
    public static boolean belongsToUser(Account account, Long userId) {
        return account.getUserId().equals(userId);
    }

}
