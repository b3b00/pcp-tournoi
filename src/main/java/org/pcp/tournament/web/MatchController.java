package org.pcp.tournament.web;

import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Match;
import org.pcp.tournament.model.MatchSet;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.MatchService;
import org.pcp.tournament.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
public class MatchController {

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

    @GetMapping(value = "/match/{matchId}")
    public Match getMatch(@PathVariable int tournamentId, @PathVariable int matchId) {
        Match match = matchDao.findById(matchId);
        return match;
    }

    // endregion

    // region [POST]

    @PostMapping(value = "/tournament/{tournamentId}/match")
    public Match createMatch(@PathVariable int tournamentId, @RequestBody Match match) throws NotFoundException {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            Match newMatch = matchService.createMatch(match, tournament.getOptions());
            return newMatch;
        }
        throw new NotFoundException("tournament not found");
    }

    // endregion

    @PutMapping(value = "/tournament/{tournamentId}/match")
    public Match updateMatch(@PathVariable int tournamentId, @RequestBody Match match) throws NotFoundException {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            Match realMatch = matchDao.findById(match.getId());
            //Match newMatch = matchDao.save(match);
            
            if (realMatch != null) {
                for (int i = 0; i < match.getScore().size(); i++) {
                    MatchSet set = match.getSet(i);
                    set.setMatch(realMatch);
                    matchSetDao.save(set);
                }
                Match newMatch = matchDao.save(realMatch);
                newMatch.compute(tournament.getOptions());                
                runService.InjectTeams(tournament);
                runService.computeTeamReferenceLabels(tournament);
                newMatch = matchDao.findById(newMatch.getId());
                newMatch.getLeftTeamReferenceLabel();
                newMatch.getRightTeamReferenceLabel();
                return newMatch;
            }
            throw new NotFoundException("match not found");
        }

        throw new NotFoundException("tournament not found");

    }
    // endregion

    

}
