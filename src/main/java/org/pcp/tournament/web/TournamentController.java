package org.pcp.tournament.web;

import java.util.List;

import javax.websocket.server.PathParam;

import org.pcp.tournament.DataLoader;
import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.service.RunService;
import org.pcp.tournament.web.exception.PCPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController extends PCPController {

    @Autowired
    OptionsDao optionsDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TeamDao teamDao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    DataLoader dataLoader;

    @Autowired
    RunService runService;

    @GetMapping(value = "/tournaments")
    
    public ResponseEntity<?> all(Authentication authentication) throws PCPException {
        Identity identity = checkIdentity(authentication);
        List<Tournament> tournaments = tournamentDao.findByOwner(identity.getId());
        return new ResponseEntity<List<Tournament>>(tournaments,HttpStatus.OK);
    }

    @GetMapping(value = "/tournaments/{id}")
    public ResponseEntity<?> getTournament(@PathVariable int id, final Authentication authentication) throws PCPException {
        Identity identity = checkIdentity(authentication,id);
        Tournament tournament = tournamentDao.findById(id);
        if (!tournament.getOwner().equals(identity.getId())) {
            return new ResponseEntity<String>("non authentitfié.", HttpStatus.FORBIDDEN);
        }
        runService.computeTeamReferenceLabels(tournament);
        if (tournament.getRun() != null && tournament.getRun().getGroupPhase() != null) {
            GroupPhase groupPhase = tournament.getRun().getGroupPhase();
            if (groupPhase.getGroups() != null) {
                for (GroupPlay group : groupPhase.getGroups()) {
                    if (group != null) {
                        group.computeRanking();
                    }
                }
            }
            if (tournament.getRun().getBoard() != null) {
                tournament.getRun().getBoard().computeScores(tournament.getOptions());
                runService.InjectTeams(tournament);
                runService.computeTeamReferenceLabels(tournament);
                tournament = tournamentDao.findById(tournament.getId());
            }
        }
        return new ResponseEntity<Tournament>(tournament,HttpStatus.OK);
    }

    @GetMapping(value="/tournaments/fake")
    public Tournament fake(@PathParam("count") int count,
                           @PathParam("name")String name,
                           @PathParam("mode") String type,
                           @PathParam("play") boolean play,
                           @PathParam("grpsize") int grpsize,
                           final Authentication authentication) throws PCPException {
        checkIdentity(authentication);
        Mode mode = type != null ? (type == "S" ? Mode.SINGLE : Mode.DOUBLE) : Mode.SINGLE;
        Options options = optionsDao.findByMode(mode);
        Options optionsreal = new Options();
        if (mode == Mode.SINGLE) {
            optionsreal.setMode(mode);
            optionsreal.setWinningSets(1);
            optionsreal.setSetLength(11);
            optionsreal = optionsDao.save(optionsreal);
            options = optionsreal;
        }
        
        Tournament tournament = new Tournament(name);
        tournament.setOptions(options);
        tournament = tournamentDao.save(tournament);
        tournament = dataLoader.buildFake(tournament, count, grpsize);        
        if (play) {
            tournament = dataLoader.buildFakeRun(tournament, play);
        }
        return tournament;
    }


    @DeleteMapping("/tournaments/{tournamentId}/run")
    public Tournament deleteRun(@PathVariable int tournamentId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
        runService.deleteRunForTournament(tournamentId);
        return tournamentDao.findById(tournamentId);
    }

    @DeleteMapping("/tournament/{tournamentId}")
    public ResponseEntity<?> deleteTournament(@PathVariable int tournamentId, final Authentication authentication) throws PCPException  {
        
        Identity identity = checkIdentity(authentication, tournamentId);
        if (identity == null || !identity.IsOk()) {
            return new ResponseEntity<String>("non authentitfié.", HttpStatus.FORBIDDEN);
        }
        
            tournamentDao.deleteById(tournamentId);
        
        return all(authentication);
    }

    @GetMapping("/tournaments/deleteAll")
    public void deleteAll(final Authentication authentication) throws PCPException {
        checkIdentity(authentication);
        try {
            List<Team> teams = teamDao.findAll();
            teams.stream().forEach(t -> {
                t.setPlayer1(null);
                t.setPlayer2(null);
                teamDao.save(t);
            });
            List<Tournament> tournaments = tournamentDao.findAll();
            tournaments.stream().forEach(t -> {
                t.getPlayers().clear();
                t.getTeams().clear();
                t.getGroups().clear();
                tournamentDao.save(t);
            });
            teamDao.deleteAll();
            playerDao.deleteAll();
            groupDao.deleteAll();
            tournamentDao.deleteAll();
            optionsDao.deleteAll();
        } catch (Exception e) {
            throw new InternalError("error clearing db :  " + e.getMessage());
        }

    }

}
