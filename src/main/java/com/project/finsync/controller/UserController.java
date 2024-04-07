package com.project.finsync.controller;

import com.project.finsync.model.User;
import com.project.finsync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return userService.findUserById(id)
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return createdUser != null ? ResponseEntity.status(HttpStatus.CREATED).body(createdUser) :
                ResponseEntity.badRequest().build();

    }

    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User newUser) {
        newUser.setId(id);
        return userService.updateUser(newUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}