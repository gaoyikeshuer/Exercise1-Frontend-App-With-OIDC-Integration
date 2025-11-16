package com.example.frontendapp.configuration;

import com.example.frontendapp.security.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Configuration
public class SecurityConfig {
    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }


    @Value("${app.security.required-role}")
    private String requiredRole;

    @Value("${app.security.mfa-role}")
    private String mfaRole;

    @Value("${app.security.require-mfa:true}")
    private boolean requireMfa;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/calendar/**").access(this::checkAccess)
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(customOidcUserService)
                        ))
                .oauth2Client(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

        return http.build();
    }
    /**
     * Centralized access rule for /calendar endpoints.
     * Checks required role and MFA if enabled.
     */
    private AuthorizationDecision checkAccess(Supplier<Authentication> authentication,
                                              RequestAuthorizationContext context) {

        Authentication auth = authentication.get();
        if (auth == null || !(auth.getPrincipal() instanceof OidcUser user)) {
            return new AuthorizationDecision(false);
        }

        boolean hasRole = KeycloakUtils.hasRole(user, requiredRole);
        boolean hasMfa = KeycloakUtils.hasRole(user, mfaRole);
        boolean allowedAccess = requireMfa? (hasRole && hasMfa) : hasRole;

        return new AuthorizationDecision(allowedAccess);
    }
}
