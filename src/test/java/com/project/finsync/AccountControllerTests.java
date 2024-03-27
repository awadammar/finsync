package com.project.finsync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.finsync.controller.AccountController;
import com.project.finsync.enums.AccountType;
import com.project.finsync.model.Account;
import com.project.finsync.model.User;
import com.project.finsync.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password");
        user.setId(1L);

        account = new Account(user);
        account.setId(1L);
    }

    @Test
    void testFindAllAccountsForUser() throws Exception {
        when(accountService.findAccountsByUser(1L)).thenReturn(Collections.singletonList(account));

        mockMvc.perform(get("/users/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindAccountByIdForUser_AccountExists() throws Exception {
        when(accountService.findAccountByIdAndUser(1L, 1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/users/1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testFindAccountByIdForUser_AccountNotFound() throws Exception {
        when(accountService.findAccountByIdAndUser(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1/accounts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccount_ValidAccount() throws Exception {
        when(accountService.createAccount(anyLong(), any(Account.class))).thenReturn(Optional.of(account));

        mockMvc.perform(post("/users/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCreateAccount_InvalidAccountData() throws Exception {
        mockMvc.perform(post("/users/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateAccount_ServiceFailure() throws Exception {
        when(accountService.createAccount(1L, account)).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateAccount_AccountUpdated() throws Exception {
        Account updatedAccount = new Account(user, "testAcc", AccountType.BUSINESS);
        updatedAccount.setId(1L);

        when(accountService.updateAccount(anyLong(), anyLong(), any(Account.class))).thenReturn(Optional.of(updatedAccount));

        mockMvc.perform(put("/users/1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(AccountType.BUSINESS.name()));
    }

    @Test
    void testUpdateAccount_AccountNotFound() throws Exception {
        when(accountService.updateAccount(1L, 1L, account)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAllAccountsForUser_AccountsDeleted() throws Exception {
        doNothing().when(accountService).deleteAllAccountsByUserId(1L);

        mockMvc.perform(delete("/users/1/accounts"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAccountByIdForUser_AccountDeleted() throws Exception {
        doNothing().when(accountService).deleteAccountByIdAndUserId(1L, 1L);

        mockMvc.perform(delete("/users/1/accounts/1"))
                .andExpect(status().isNoContent());
    }
}
