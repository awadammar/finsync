package com.project.finsync.domain.model.repository;

import com.project.finsync.domain.model.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
