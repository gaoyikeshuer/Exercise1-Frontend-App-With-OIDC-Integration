package com.example.frontendapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.*;

public class CustomOidcUser implements OidcUser {

    private final OidcUser delegate;
    private final Map<String, Object> accessTokenClaims;

    public CustomOidcUser(OidcUser delegate, Map<String, Object> accessTokenClaims) {
        this.delegate = delegate;
        this.accessTokenClaims = accessTokenClaims;
    }

    public Map<String, Object> getAccessTokenClaims() {
        return accessTokenClaims;
    }
    @Override
    public Map<String, Object> getAttributes() {

        // merge ID token claims + access token claims cleanly
        Map<String, Object> merged = new HashMap<>(delegate.getAttributes());
        merged.put("access_token_claims", accessTokenClaims);

        return merged;
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}


