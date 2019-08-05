package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.model.dto.NameAndOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayersController {

    @Autowired
    PlayerDao playersDao;

    @Autowired
    TournamentDao tournamentDao;

    @GetMapping(value = "/tournament/{tournamentId}/players")
    public List<Player> getAll(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Player> players = tournament.getPlayers();
        return players;
    }

    @PostMapping(value = "/tournament/{tournamentId}/players")
    public void updatePlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            Player newPlayer = playersDao.save(player);
            tournament.getPlayers().add(player);
            tournamentDao.save(tournament);
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @PutMapping(value = "/tournament/{tournamentId}/players")
    public void addPlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {            
            Player newPlayer = playersDao.save(player);            
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

}
