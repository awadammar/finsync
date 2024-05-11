package com.project.finsync.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.finsync.enums.ExpenseCategory;
import com.project.finsync.model.Budget;
import com.project.finsync.model.User;
import com.project.finsync.service.BudgetService;
import com.project.finsync.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Month;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
@AutoConfigureMockMvc(addFilters = false)
class BudgetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private Budget budget;

    @BeforeEach
    void setUp() {
        user = TestUtils.createSimpleUser();

        budget = new Budget(user, 500.5, Month.JANUARY, ExpenseCategory.GROCERIES);
        budget.setId(1L);
    }

    @Test
    void testFindBudgetsForUser() throws Exception {
        when(budgetService.findBudgetsByUser(1L)).thenReturn(Collections.singletonList(budget));

        mockMvc.perform(get("/users/1/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindBudgetByIdForUser() throws Exception {
        when(budgetService.findBudgetByIdAndUser(1L, 1L)).thenReturn(Optional.of(budget));

        mockMvc.perform(get("/users/1/budgets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testFindBudgetsByMonthForUser() throws Exception {
        when(budgetService.findBudgetsByUserByMonth(1L, Month.JANUARY)).thenReturn(Collections.singletonList(budget));

        mockMvc.perform(get("/users/1/budgets/month/JANUARY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindBudgetByCategoryForUser() throws Exception {
        when(budgetService.findBudgetsByUserByCategory(1L, ExpenseCategory.GROCERIES)).thenReturn(Collections.singletonList(budget));

        mockMvc.perform(get("/users/1/budgets/category/GROCERIES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testFindTotalAmountForBudgets() throws Exception {
        when(budgetService.findTotalAmountOfBudgets(1L)).thenReturn(1000.0);

        mockMvc.perform(get("/users/1/budgets/total-amount"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    void testCreateBudget() throws Exception {
        when(budgetService.createBudget(anyLong(), any(Budget.class))).thenReturn(Optional.of(budget));

        mockMvc.perform(post("/users/1/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testUpdateBudget() throws Exception {
        Budget updatedBudget = new Budget(user, 500.5, Month.DECEMBER, ExpenseCategory.GROCERIES);
        updatedBudget.setId(1L);

        when(budgetService.updateBudget(anyLong(), anyLong(), any(Budget.class))).thenReturn(Optional.of(updatedBudget));

        mockMvc.perform(put("/users/1/budgets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.month").value(updatedBudget.getMonth().name()));

    }

    @Test
    void testDeleteAllBudgetsByCategoryForUser() throws Exception {
        mockMvc.perform(delete("/users/1/budgets/category/GROCERIES"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBudget() throws Exception {
        mockMvc.perform(delete("/users/1/budgets/1"))
                .andExpect(status().isNoContent());
    }

}
