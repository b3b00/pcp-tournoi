package org.pcp.tournament.web;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class PlayersController {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    PlayersService playersService;

    @Autowired
    TeamDao teamDao;

    @GetMapping(value = "/tournament/{tournamentId}/players")
    public List<Player> getAll(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Player> players = tournament.getPlayers();
        return players;
    }

    @PostMapping(value = "/tournament/{tournamentId}/players")
    public Player addPlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            player.setTournament(tournament);
            Player newPlayer = playerDao.save(player);
            
            tournament.getPlayers().add(newPlayer);
            tournamentDao.save(tournament);
            return newPlayer;
        } catch (Exception e) {
            throw new InternalError(e.getMessage());
        }
    }

    @PutMapping(value = "/tournament/{tournamentId}/players")
    public Player updatePlayer(@PathVariable int tournamentId, @RequestBody Player player) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            player.setTournament(tournament);
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

    @PostMapping("/tournaments/{tournamentId}/playersUpload")
    public ResponseEntity<?> uploadPlayers(@PathVariable int tournamentId, @RequestParam("file") MultipartFile file) {
        try {
            Resource resource = file.getResource();
            byte[] bytes = file.getBytes();
            String content = new String(bytes);
            Tournament tournament = tournamentDao.findById(tournamentId);
            playersService.ImportPlayers(tournament, content);
            tournament = tournamentDao.findById(tournamentId);            
            return new ResponseEntity<Tournament>(tournament,HttpStatus.OK);
        }
        catch(DataIntegrityViolationException dive) {
            return ResponseEntity.badRequest()
            .body(getErrorJSON("impossible de charger le fichier "+file.getOriginalFilename()+" : mauvais format."));
        }
        catch(Exception e) {
            return ResponseEntity.badRequest()
            .body(getErrorJSON("impossible de charger le fichier "+file.getOriginalFilename()));
            
        }

    }

    public String getErrorJSON(String message) {
        return "{\"message\":\""+message+"\"}";
    }


}
