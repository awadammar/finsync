package com.project.finsync.domain.model.repository;

import com.project.finsync.domain.model.entity.Reminder;
import org.springframework.data.repository.CrudRepository;

public interface ReminderRepository extends CrudRepository<Reminder, Long> {

}
