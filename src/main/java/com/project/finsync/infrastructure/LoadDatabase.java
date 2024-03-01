package com.project.finsync.infrastructure;

import com.project.finsync.domain.model.entity.User;
import com.project.finsync.domain.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
            log.info("Preloading " + userRepository.save(new User("Bilbo Baggins")));
            log.info("Preloading " + userRepository.save(new User("Frodo Baggins")));
        };
    }
}
