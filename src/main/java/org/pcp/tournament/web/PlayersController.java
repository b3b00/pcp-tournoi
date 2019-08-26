package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayersController {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    TeamDao teamDao;

    @GetMapping(value = "/tournament/{tournamentId}/players")
    public List<Player> getAll(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Player> players = tournament.getPlayers();
        return players;
    }

    @PostMapping(value = "/tournament/{tournamentId}/players")
    public Player updatePlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            Player newPlayer = playerDao.save(player);
            newPlayer.setTournament(tournament);
            tournament.getPlayers().add(newPlayer);            
            tournamentDao.save(tournament);
            return newPlayer;
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @PutMapping(value = "/tournament/{tournamentId}/players")
    public Player addPlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {
            Player updatedPlayer = playerDao.save(player);
            return updatedPlayer;
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @DeleteMapping("/tournament/{tournamentId}/players/{playerId}")
    public void deletePlayer(@PathVariable int tournamentId, @PathVariable int playerId) {
        try {
            Player player = playerDao.findById(playerId);
            Tournament tournament = tournamentDao.findById(tournamentId);
            tournament.getPlayers().removeIf(p -> p.getId() == playerId);
            tournamentDao.save(tournament);
            Team team = teamDao.findWithPlayer(player);
            if (team != null) {
                team.RemovePlayer(player);
                teamDao.save(team);
            }
            playerDao.deleteById(playerId);

        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

}
