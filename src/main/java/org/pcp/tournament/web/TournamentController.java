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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {

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
    public List<Tournament> all() {
        List<Tournament> tournaments = tournamentDao.findAll();
        return tournaments;
    }

    @GetMapping(value = "/tournaments/{id}")
    public Tournament getTournament(@PathVariable int id) {
        Tournament tournament = tournamentDao.findById(id);
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
        return tournament;
    }

    @GetMapping(value="/tournaments/fake")
    public Tournament fake(@PathParam("count") int count,
                           @PathParam("name")String name,
                           @PathParam("mode") String type,
                           @PathParam("play") boolean play,
                           @PathParam("grpsize") int grpsize) {
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
    public Tournament deleteRun(@PathVariable int tournamentId) {
        runService.deleteRunForTournament(tournamentId);
        return tournamentDao.findById(tournamentId);
    }

    @DeleteMapping("/tournament/{tournamentId}")
    public List<Tournament> deleteTournament(@PathVariable int tournamentId) {
        tournamentDao.deleteById(tournamentId);
        return all();
    }

    @GetMapping("/tournaments/deleteAll")
    public void deleteAll() {
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
