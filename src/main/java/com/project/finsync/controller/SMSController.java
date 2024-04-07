package com.project.finsync.controller;

import com.project.finsync.model.Transaction;
import com.project.finsync.model.User;
import com.project.finsync.service.SMSProcessorService;
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
public class SMSController {
    private final SMSProcessorService smsProcessorService;

    @PostMapping
    public ResponseEntity<Transaction> processSmsForAccount(@RequestBody List<String> smsList, User user) {
        smsList.forEach(smsProcessorService::convertSMSToTransaction);
        return ResponseEntity.ok().build();
    }
}
