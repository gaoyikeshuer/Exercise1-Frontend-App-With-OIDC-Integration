package com.example.frontendapp.controller;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendar(@AuthenticationPrincipal OidcUser user, Model model) {

        // Get user’s access token
        String accessToken = user.getIdToken().getTokenValue();

        // Prepare Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Call the protected calendar service
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:9090/api/calendar",
                HttpMethod.GET,
                request,
                String.class
        );

        // Prepare data for frontend
        model.addAttribute("username", user.getPreferredUsername());
        model.addAttribute("calendarData", response.getBody());

        return "calendar";
    }
}
