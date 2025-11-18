package com.example.frontendapp.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final JwtDecoder jwtDecoder;

    public CustomOidcUserService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {

        OidcUser oidcUser = super.loadUser(userRequest);
        String tokenValue = userRequest.getAccessToken().getTokenValue();
        Jwt accessTokenJwt = jwtDecoder.decode(tokenValue);
        // System.out.println("ACCESS TOKEN = " + tokenValue);

        return new CustomOidcUser(
                oidcUser,
                accessTokenJwt.getClaims()
        );
    }
}

