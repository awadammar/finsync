package com.project.finsync.config;

import com.project.finsync.model.User;
import com.project.finsync.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private UserRepository userRepository;

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
            log.info("Preloading {}", userRepository.save(new User("Bilbo Baggins", "a@b.com", "nonSafePwd")));
            log.info("Preloading {}", userRepository.save(new User("Frodo Baggins", "c@d.com", "someWhatSafePwd")));
        };
    }
}
