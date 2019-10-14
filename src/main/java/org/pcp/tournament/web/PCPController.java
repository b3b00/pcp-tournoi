package org.pcp.tournament.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Map;

import com.auth0.jwt.interfaces.Claim;

import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.security.TokenAuthentication;
import org.pcp.tournament.web.exception.PCPError;
import org.pcp.tournament.web.exception.PCPException;

public class PCPController {

    @Autowired
    TournamentDao tournamentDao;

    public Identity checkIdentity(final Authentication authentication) throws PCPException {

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            throw new PCPException(PCPError.UNAUTHORIZED,"Forbidden, not connected.");
        }

        Map<String, Claim> claims = tokenAuthentication.getClaims();
        String name = claims.get("name").asString();
        String sub = claims.get("sub").asString();

        return new Identity(sub, name);

    }

    public Identity checkIdentity(final Authentication authentication, Tournament tournament) throws PCPException {

        Identity identity = checkIdentity(authentication);

        if (!identity.getId().equals(tournament.getOwner())) {
            throw new PCPException(PCPError.UNAUTHORIZED,"illegal access.");
        }

        return identity;

    }

    
    public Identity checkIdentity(final Authentication authentication, int tournamentId) throws PCPException {

        Identity identity = checkIdentity(authentication);

        if (!tournamentDao.IsOwner(tournamentId, identity.getId())) {
            throw new PCPException(PCPError.UNAUTHORIZED,"illegal access.");
        }

        return identity;

    }
}