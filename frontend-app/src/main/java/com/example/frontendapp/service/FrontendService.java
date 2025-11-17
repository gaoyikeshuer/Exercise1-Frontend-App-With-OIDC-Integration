package com.example.frontendapp.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;

public interface FrontendService {
    String showCalendar(@AuthenticationPrincipal OidcUser user, Model model);
}
