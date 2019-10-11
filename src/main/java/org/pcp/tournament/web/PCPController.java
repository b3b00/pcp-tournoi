package org.pcp.tournament.web;

import org.springframework.security.core.Authentication;

import java.util.Map;

import com.auth0.jwt.interfaces.Claim;

import org.pcp.tournament.security.TokenAuthentication;
import org.pcp.tournament.util.TokenUtils;

public class PCPController {

    public Identity checkIdentity(final Authentication authentication) {

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            return new Identity();
        }

        Map<String, Claim> claims = tokenAuthentication.getClaims();
        String name = claims.get("name").asString();
        String sub = claims.get("sub").asString();

        return new Identity(sub, name);

    }
}