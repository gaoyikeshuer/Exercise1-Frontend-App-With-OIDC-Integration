package com.example.frontendapp.configuration;

import com.example.frontendapp.security.CustomOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class KeycloakUtils {

    private KeycloakUtils() {}

    public static boolean hasRole(OidcUser user, String requiredRole) {

        if (!(user instanceof CustomOidcUser customUser))
            return false;

        Map<String, Object> claims = customUser.getAccessTokenClaims();
        if (claims == null)
            return false;

        // --------------------------
        // 1) Check Realm Roles
        // --------------------------
        Object realmAccessObj = claims.get("realm_access");
        if (realmAccessObj instanceof Map<?, ?> realmAccess) {

            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> rolesList) {

                if (rolesList.contains(requiredRole)) {
                    return true;
                }
            }
        }

        // --------------------------
        // 2) Check Client Roles
        // --------------------------
        Object resourceAccessObj = claims.get("resource_access");
        if (resourceAccessObj instanceof Map<?, ?> clientRolesMap) {

            for (Object clientObj : clientRolesMap.values()) {

                if (clientObj instanceof Map<?, ?> clientRoleEntry) {

                    Object rolesObj = clientRoleEntry.get("roles");

                    if (rolesObj instanceof List<?> rolesList) {

                        if (rolesList.contains(requiredRole)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
