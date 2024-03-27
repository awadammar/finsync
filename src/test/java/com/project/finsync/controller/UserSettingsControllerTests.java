package com.project.finsync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.finsync.TestUtils;
import com.project.finsync.model.User;
import com.project.finsync.model.UserSettings;
import com.project.finsync.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSettingsController.class)
class UserSettingsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSettingsService userSettingsService;

    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setUp() {
        user = TestUtils.createSimpleUser();
    }

    @Test
    void testFindSettingsForUser_SettingsExist() throws Exception {
        UserSettings settings = new UserSettings();
        settings.setUser(user);
        settings.setPushNotification(true);

        when(userSettingsService.findSettingsByUserId(1L)).thenReturn(Optional.of(settings));

        mockMvc.perform(get("/users/1/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.pushNotification").value(true));
    }

    @Test
    void testFindSettingsForUser_SettingsNotExist() throws Exception {
        when(userSettingsService.findSettingsByUserId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/settings"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSettings_SettingsUpdated() throws Exception {
        UserSettings updatedUserSettings = new UserSettings(user);
        updatedUserSettings.setPushNotification(false);

        when(userSettingsService.updateSettings(anyLong(), any(UserSettings.class))).thenReturn(Optional.of(updatedUserSettings));

        mockMvc.perform(put("/users/1/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserSettings)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.pushNotification").value(updatedUserSettings.getPushNotification()));
    }

    @Test
    void testUpdateSettings_SettingsNotFound() throws Exception {
        UserSettings newUserSettings = new UserSettings();
        newUserSettings.setUser(user);
        newUserSettings.setPushNotification(true);

        when(userSettingsService.updateSettings(1L, newUserSettings)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserSettings)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSettingsForUser_SettingsDeleted() throws Exception {
        doNothing().when(userSettingsService).deleteByUserId(1L);

        mockMvc.perform(delete("/users/1/settings"))
                .andExpect(status().isNoContent());
    }
}
