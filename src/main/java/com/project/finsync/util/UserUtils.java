package com.project.finsync.util;

import com.project.finsync.model.Account;
import com.project.finsync.model.User;

public class UserUtils {

    private UserUtils() {}

    public static boolean belongsToUser(Account account, User user) {
        return account.getUser().equals(user);
    }

}
