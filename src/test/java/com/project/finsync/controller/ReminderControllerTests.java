package com.project.finsync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.finsync.TestUtils;
import com.project.finsync.enums.ReminderStatus;
import com.project.finsync.model.Reminder;
import com.project.finsync.model.User;
import com.project.finsync.service.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.project.finsync.enums.ReminderType.BILL_PAYMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReminderController.class)
class ReminderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReminderService reminderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reminder reminder;

    @BeforeEach
    void setUp() {
        User user = TestUtils.createSimpleUser();

        reminder = new Reminder(user, BILL_PAYMENT, "");
        reminder.setReminderId(1L);
    }

    @Test
    void testFindAllRemindersForUser() throws Exception {
        when(reminderService.findRemindersByUser(1L)).thenReturn(Collections.singletonList(reminder));

        mockMvc.perform(get("/users/1/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reminderId").exists());
    }

    @Test
    void testFindRemindersByStatusForUser() throws Exception {
        when(reminderService.findRemindersByUserByStatus(1L, ReminderStatus.ACTIVE)).thenReturn(Collections.singletonList(reminder));

        mockMvc.perform(get("/users/1/reminders/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reminderId").exists());
    }

    @Test
    void testFindRemindersByDateForUser() throws Exception {
        LocalDate date = LocalDate.now();
        when(reminderService.findRemindersByUserByDate(1L, date)).thenReturn(Collections.singletonList(reminder));

        mockMvc.perform(get("/users/1/reminders/date/" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reminderId").exists());
    }

    @Test
    void testCreateReminderForUser_ValidReminder() throws Exception {
        when(reminderService.createReminder(anyLong(), any(Reminder.class))).thenReturn(Optional.of(reminder));

        mockMvc.perform(post("/users/1/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reminderId").exists());
    }

    @Test
    void testCreateReminderForUser_InvalidReminderData() throws Exception {
        mockMvc.perform(post("/users/1/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateReminderForUser_ServiceFailure() throws Exception {
        when(reminderService.createReminder(1L, reminder)).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateReminder_ReminderUpdated() throws Exception {
        Reminder updatedReminder = reminder;
        updatedReminder.setReminderId(1L);
        when(reminderService.updateReminder(anyLong(), anyLong(), any(Reminder.class))).thenReturn(Optional.of(updatedReminder));

        mockMvc.perform(put("/users/1/reminders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reminder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reminderId").value(1));
    }

    @Test
    void testMarkReminderAsComplete_ReminderMarkedAsComplete() throws Exception {
        mockMvc.perform(put("/users/1/reminders/1/complete"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReminder_ReminderDeleted() throws Exception {
        mockMvc.perform(delete("/users/1/reminders/1"))
                .andExpect(status().isNoContent());
    }

}
