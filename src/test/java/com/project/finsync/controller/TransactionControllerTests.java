package com.project.finsync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.finsync.TestUtils;
import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.enums.TransactionType;
import com.project.finsync.model.Account;
import com.project.finsync.model.Transaction;
import com.project.finsync.model.User;
import com.project.finsync.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account account;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        User user = TestUtils.createSimpleUser();

        account = new Account(user);
        account.setId(1L);

        transaction = new Transaction(account, 1000.0, LocalDate.of(2024, 12, 12), TransactionType.OTHER, ExpenseCategory.GROCERIES);
        transaction.setId(1L);
    }

    @Test
    void testFindTransactionByIdForAccount() throws Exception {
        when(transactionService.findTransactionByIdAndAccount(1L, 1L)).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/accounts/1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testFindTransactionsByAccountId() throws Exception {
        when(transactionService.findTransactionsByAccount(1L)).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }


    @Test
    void testFindTransactionsByAccountByMonth() throws Exception {
        when(transactionService.findTransactionsByAccountByMonth(1L, Month.JANUARY)).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/accounts/1/transactions/month/JANUARY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindTransactionsByAccountByType() throws Exception {
        when(transactionService.findTransactionsByAccountByType(1L, TransactionType.CREDITED)).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/accounts/1/transactions/type/CREDITED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindTransactionsByAccountByCategory() throws Exception {
        when(transactionService.findTransactionsByAccountByCategory(1L, ExpenseCategory.GROCERIES)).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/accounts/1/transactions/category/GROCERIES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindTransactionsByAccountByTags() throws Exception {
        when(transactionService.findTransactionsByAccountByTags(1L, Collections.singleton("tag1"))).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/accounts/1/transactions/tags").param("tags", "tag1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testGetTotalAmountByAccount() throws Exception {
        when(transactionService.getTotalAmountByAccount(1L)).thenReturn(1000.0);

        mockMvc.perform(get("/accounts/1/transactions/total-amount"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    void testCreateTransaction() throws Exception {
        when(transactionService.createTransaction(anyLong(), any(Transaction.class))).thenReturn(Optional.of(transaction));

        mockMvc.perform(post("/accounts/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUpdateTransaction() throws Exception {
        Transaction updatedTransaction = new Transaction(account, 1000.0, LocalDate.of(2024, 12, 12), TransactionType.OTHER, ExpenseCategory.EDUCATION);
        updatedTransaction.setId(1L);

        when(transactionService.updateTransaction(anyLong(), anyLong(), any(Transaction.class))).thenReturn(Optional.of(updatedTransaction));

        mockMvc.perform(put("/accounts/1/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.category").value(updatedTransaction.getCategory().name()));
    }

    @Test
    void testDeleteAllTransactionsForAccount() throws Exception {
        mockMvc.perform(delete("/accounts/1/transactions"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/accounts/1/transactions/1"))
                .andExpect(status().isNoContent());
    }

}
