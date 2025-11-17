package com.example.calendarservice.configuration;

import com.example.calendarservice.controller.CalendarController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc   // keep Security filters enabled
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void whenNoJwt_thenCalendarIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().isUnauthorized());   // 401
    }

    @Test
    void whenJwtProvided_thenCalendarReturnsOk() throws Exception {
        mockMvc.perform(get("/api/calendar").with(jwt())) // mock JWT
                .andExpect(status().isOk());              // 200
    }
}
