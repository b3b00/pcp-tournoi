package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.PlayersService;
import org.pcp.tournament.web.exception.PCPError;
import org.pcp.tournament.web.exception.PCPException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
public class PlayersController extends PCPController {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    PlayersService playersService;

    @Autowired
    TeamDao teamDao;

    @GetMapping(value = "/tournament/{tournamentId}/players")
    public List<Player> getAll(@PathVariable int tournamentId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication);
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Player> players = tournament.getPlayers();
        return players;
    }

    @PostMapping(value = "/tournament/{tournamentId}/players")
    public Player addPlayer(@PathVariable int tournamentId, @RequestBody Player player, final Authentication authentication) throws PCPException {
        checkIdentity(authentication, tournamentId);
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
    public Player updatePlayer(@PathVariable int tournamentId, @RequestBody Player player, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
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
    public void deletePlayer(@PathVariable int tournamentId, @PathVariable int playerId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication, tournamentId);
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

    @PostMapping("/tournaments/{tournamentId}/players/upload")
    public ResponseEntity<?> uploadPlayers(@PathVariable int tournamentId, @RequestParam("file") MultipartFile file, final Authentication authentication) throws PCPException {
        checkIdentity(authentication, tournamentId);
        try {
            byte[] bytes = file.getBytes();
            String content = new String(bytes);
            Tournament tournament = tournamentDao.findById(tournamentId);
            playersService.ImportPlayers(tournament, content);
            tournament = tournamentDao.findById(tournamentId);            
            return new ResponseEntity<Tournament>(tournament,HttpStatus.OK);
        }
        catch(DataIntegrityViolationException dive) {
            throw new PCPException(PCPError.BAD_FILE,"impossible de charger le fichier "+file.getOriginalFilename()+" : mauvais format.");
        }
        catch(Exception e) {
            throw new PCPException(PCPError.BAD_FILE,"impossible de charger le fichier "+file.getOriginalFilename());
            
        }
    }

    @GetMapping("/tournaments/{tournamentId}/players/download") 
    public ResponseEntity<?> exportPlayers(@PathVariable int tournamentId, final Authentication authentication)  throws PCPException {
        checkIdentity(authentication, tournamentId);
        Tournament tournament = tournamentDao.findById(tournamentId);
        String csv = playersService.playersToCSV(tournament);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type","text/csv");
        return ResponseEntity.ok()        
        .headers(responseHeaders)
        .body(csv);
    }

    public String getErrorJSON(String message) {
        return "{\"message\":\""+message+"\"}";
    }


}
