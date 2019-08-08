package org.pcp.tournament.web;

import java.util.ArrayList;
import java.util.List;

import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.TeamStrategies;
import org.pcp.tournament.service.TeamStrategiesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamsController {

    

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TeamDao teamDao;

    @GetMapping(value = "/tournaments/{tournamentId}/teams")
    public List<Team> getAll(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Team> teams = tournament.getTeams();
        return teams;
    }

    
    @PostMapping("/tournaments/{tournamentId}/teams/create/{mode}")
    public ResponseEntity<?> createTeams(@PathVariable int tournamentId, @PathVariable TeamStrategiesEnum mode) {

        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            List<Player> players = tournament.getPlayers();
            if (players.size() % 2 == 0) {
                List<Team> teams = new ArrayList<Team>();
                switch (mode) {
                    case RANDOM: {
                        teams = TeamStrategies.pureRandom(players,teamDao);
                        break;
                    }
                    case MIX: {
                        teams = TeamStrategies.mixLicensees(players,teamDao);
                        break;
                    }
                }
                tournament.setTeams(teams);
                tournament = tournamentDao.save(tournament);
                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<String>("nombre de joueur doit être pair ("+players.size()+")", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }
    

}
