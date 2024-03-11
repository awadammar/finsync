package com.project.finsync.repository;

import com.project.finsync.model.Reminder;
import com.project.finsync.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

    List<Reminder> findByUser(User user);
}
