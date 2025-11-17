package com.example.calendarservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalendarController.class)
@AutoConfigureMockMvc(addFilters = false)
class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCalendarEvents() throws Exception {
        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value("2025-11-14"))
                .andExpect(jsonPath("$[0].event").value("Keycloak Integration Demo"))
                .andExpect(jsonPath("$[1].date").value("2025-11-15"))
                .andExpect(jsonPath("$[1].event").value("Sprint Review"))
                .andExpect(jsonPath("$[2].date").value("2025-11-16"))
                .andExpect(jsonPath("$[2].event").value("Team Lunch"));
    }
}
