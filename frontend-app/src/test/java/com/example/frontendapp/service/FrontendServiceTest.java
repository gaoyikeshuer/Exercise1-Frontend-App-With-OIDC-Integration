package com.example.frontendapp.service;

import com.example.frontendapp.service.impl.FrontendServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FrontendServiceTest {

    @Test
    void showCalendar_shouldReturnCalendarAndSetModel() {

        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(
                eq("http://localhost:9090/api/calendar"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("calendar-data"));

        // mock user + ID token
        OidcUser user = mock(OidcUser.class);
        OidcIdToken token = new OidcIdToken(
                "token123",
                Instant.now(),
                Instant.now().plusSeconds(600),
                Map.of()
        );
        when(user.getIdToken()).thenReturn(token);
        when(user.getPreferredUsername()).thenReturn("alice");
        Model model = mock(Model.class);
        FrontendService service = new FrontendServiceImpl(restTemplate);
        String view = service.showCalendar(user, model);
        assertEquals("calendar", view);
        verify(model).addAttribute("username", "alice");
        verify(model).addAttribute("calendarData", "calendar-data");
    }
}
