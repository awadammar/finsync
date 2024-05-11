package com.project.finsync.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Sms {
    private String body;
    private LocalDate date;
}
