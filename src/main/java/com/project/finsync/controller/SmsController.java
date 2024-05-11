package com.project.finsync.controller;

import com.project.finsync.model.Sms;
import com.project.finsync.model.Transaction;
import com.project.finsync.model.User;
import com.project.finsync.service.SmsProcessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/processSms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsProcessorService smsProcessorService;

    @PostMapping
    public ResponseEntity<Transaction> processSmsForAccount(@Valid @RequestBody List<Sms> smsList, User user) {
        smsProcessorService.convertSmsListForUser(smsList, user);
        return ResponseEntity.ok().build();
    }
}
