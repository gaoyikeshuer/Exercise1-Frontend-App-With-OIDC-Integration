package com.example.frontendapp.controller;

import com.example.frontendapp.service.FrontendService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    private final FrontendService frontendService;

    public CalendarController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @GetMapping("/calendar")
    public String showCalendar(@AuthenticationPrincipal OidcUser user, Model model) {
    return frontendService.showCalendar(user, model);
}
}
