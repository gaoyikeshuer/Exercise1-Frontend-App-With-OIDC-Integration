package com.example.frontendapp.configuration;

import com.example.frontendapp.security.CustomOidcUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig config;
    private OidcUser user;
    private Authentication auth;

    private Method checkAccessMethod;

    @BeforeEach
    void setup() throws Exception {
        config = new SecurityConfig(mock(CustomOidcUserService.class));

        ReflectionTestUtils.setField(config, "requiredRole", "app-role");
        ReflectionTestUtils.setField(config, "mfaRole", "mfa-role");
        ReflectionTestUtils.setField(config, "requireMfa", true);

        user = mock(OidcUser.class);
        auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(user);

        checkAccessMethod = SecurityConfig.class.getDeclaredMethod(
                "checkAccess",
                Supplier.class,
                RequestAuthorizationContext.class
        );
        checkAccessMethod.setAccessible(true);
    }

    private AuthorizationDecision call(boolean hasRequiredRole, boolean hasMfaRole) {
        MockedStatic<KeycloakUtils> mocked = Mockito.mockStatic(KeycloakUtils.class);
        mocked.when(() -> KeycloakUtils.hasRole(user, "app-role")).thenReturn(hasRequiredRole);
        mocked.when(() -> KeycloakUtils.hasRole(user, "mfa-role")).thenReturn(hasMfaRole);

        Supplier<Authentication> supplier = () -> auth;

        try {
            AuthorizationDecision result =
                    (AuthorizationDecision) checkAccessMethod.invoke(config, supplier, null);

            mocked.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void denyAccessWhenRulesFail() {
        AuthorizationDecision result = call(false, false);
        assertFalse(result.isGranted());
    }

    @Test
    void allowAccessWhenRulesPass() {
        AuthorizationDecision result = call(true, true);
        assertTrue(result.isGranted());
    }
}

