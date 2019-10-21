package org.pcp.tournament.web;

import com.auth0.jwt.interfaces.Claim;

import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.User;
import org.pcp.tournament.security.TokenAuthentication;
import org.pcp.tournament.service.MatchService;
import org.pcp.tournament.service.RunService;
import org.pcp.tournament.web.exception.PCPError;
import org.pcp.tournament.web.exception.PCPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class UserController extends PCPController {

    @Autowired
    RunService runService;

    @Autowired
    MatchDao matchDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    MatchSetDao matchSetDao;

    @Autowired
    MatchService matchService;

    // region [GET]

    @GetMapping(value = "/user")
    public User getUser(final Authentication authentication) throws PCPException {
        checkIdentity(authentication);

        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            throw new PCPException(PCPError.UNAUTHORIZED,"Forbidden, not connected.");
        }

        Map<String, Claim> claims = tokenAuthentication.getClaims();
        String name = claims.get("nickname").asString();
        String eMail = claims.get("email").asString();


        User user = new User();
        user.setName(name);
        user.seteMail(eMail);
        
        return user;
    }

    // endregion

}
