package com.example.calendarservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CalendarController {

    @GetMapping("/api/calendar")
    public List<Map<String, String>> getCalendar() {
        return List.of(
                Map.of("date", "2025-11-14", "event", "Keycloak Integration Demo"),
                Map.of("date", "2025-11-15", "event", "Sprint Review"),
                Map.of("date", "2025-11-16", "event", "Team Lunch")
        );
    }
}

