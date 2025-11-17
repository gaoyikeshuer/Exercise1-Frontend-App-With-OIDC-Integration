package com.example.frontendapp.configuration;

import com.example.frontendapp.security.CustomOidcUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUtilsTest {

    @Test
    void shouldReturnFalseWhenUserNotCustomOidcUser() {
        var user = mock(org.springframework.security.oauth2.core.oidc.user.OidcUser.class);
        boolean result = KeycloakUtils.hasRole(user, "my-role");
        assertFalse(result);
    }
    @Test
    void shouldReturnFalseWhenClaimsNull() {
        var user = mock(CustomOidcUser.class);
        when(user.getAccessTokenClaims()).thenReturn(null);
        boolean result = KeycloakUtils.hasRole(user, "my-role");
        assertFalse(result);
    }
    @Test
    void shouldReturnTrueWhenRealmRoleMatches() {
        var user = mock(CustomOidcUser.class);
        Map<String, Object> claims = Map.of(
                "realm_access", Map.of(
                        "roles", List.of("my-role", "user")
                )
        );
        when(user.getAccessTokenClaims()).thenReturn(claims);
        assertTrue(KeycloakUtils.hasRole(user, "my-role"));
    }
    @Test
    void shouldReturnFalseWhenRealmRolesDoNotMatch() {
        var user = mock(CustomOidcUser.class);
        Map<String, Object> claims = Map.of(
                "realm_access", Map.of(
                        "roles", List.of("viewer", "tester")
                )
        );
        when(user.getAccessTokenClaims()).thenReturn(claims);
        assertFalse(KeycloakUtils.hasRole(user, "my-role"));
    }
}

