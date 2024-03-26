package com.project.finsync;

import com.project.finsync.controller.UserController;
import com.project.finsync.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FinsyncApplicationTests {
	@Autowired
	private UserController userController;

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(userService).isNotNull();
	}

}
