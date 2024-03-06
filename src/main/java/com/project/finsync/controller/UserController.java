package com.project.finsync.controller;

import com.project.finsync.model.User;
import com.project.finsync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Iterable<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@PathVariable Long id, @Validated @RequestBody User newUser) {
        Optional<User> updatedUser = userService.updateUser(id, newUser);
        return updatedUser.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}