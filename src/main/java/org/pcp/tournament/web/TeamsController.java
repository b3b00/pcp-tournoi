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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping(value = "/tournaments/{tournamentId}/teams/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable int tournamentId, @PathVariable int teamId,
            @RequestBody Team team) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            Team updatedTeam = teamDao.findById(teamId);
            if (team != null) {
                updatedTeam.setPlayer1(team.getPlayer1());                
                updatedTeam.setPlayer2(team.getPlayer2());
                teamDao.save(updatedTeam);

                if (updatedTeam.isEmpty()) {
                    tournament.getTeams().removeIf(t -> t.isEmpty());
                    tournamentDao.save(tournament);
                    teamDao.delete(updatedTeam);
                }
                tournament = tournamentDao.findById(tournamentId);
                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
            }
            return new ResponseEntity<String>("l'équipe " + teamId + " n'existe pas.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/tournaments/{tournamentId}/teams/{teamID}/delete")
    public ResponseEntity<?> clearTeam(@PathVariable int tournamentId, @PathVariable int teamId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            Team team = teamDao.findById(teamId);
            if (team != null) {
                tournament.getTeams().removeIf(t -> t.getId() == teamId);
                tournament = tournamentDao.save(tournament);
                teamDao.delete(team);
                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
            }
            return new ResponseEntity<String>("l'équipe " + teamId + " n'existe pas.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/tournaments/{tournamentId}/teams/delete")
    public ResponseEntity<?> clearTeams(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            List<Team> teams = tournament.getTeams();
            tournament.getTeams().clear();
            tournament = tournamentDao.save(tournament);
            for (Team team : teams) {
                teamDao.delete(team);
            }
            return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/tournaments/{tournamentId}/teams/create/{mode}")
    public ResponseEntity<?> createTeams(@PathVariable int tournamentId, @PathVariable TeamStrategiesEnum mode) {

        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            List<Player> players = tournament.getPlayers();
            if (!tournament.getTeams().isEmpty()) {
                clearTeams(tournamentId);
            }
            if (players.size() % 2 == 0) {
                List<Team> teams = new ArrayList<Team>();
                switch (mode) {
                case RANDOM: {
                    teams = TeamStrategies.pureRandom(players, teamDao);
                    break;
                }
                case MIX: {
                    teams = TeamStrategies.mixLicensees(players, teamDao);
                    break;
                }
                }
                tournament.setTeams(teams);
                tournament = tournamentDao.save(tournament);
                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("nombre de joueur doit être pair (" + players.size() + ")",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

}
