package com.example.frontendapp.service.impl;

import com.example.frontendapp.service.FrontendService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

@Service
public class FrontendServiceImpl implements FrontendService {

    private final RestTemplate restTemplate;

    public FrontendServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String showCalendar(@AuthenticationPrincipal OidcUser user, Model model) {
        String accessToken = user.getIdToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:9090/api/calendar",
                HttpMethod.GET,
                request,
                String.class
        );
        model.addAttribute("username", user.getPreferredUsername());
        model.addAttribute("calendarData", response.getBody());

        return "calendar";
    }
}

