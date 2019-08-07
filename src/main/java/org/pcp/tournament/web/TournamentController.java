package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.DataLoader;
import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/tournaments")
    public List<Tournament> Preset() {
        List<Tournament> tournaments = tournamentDao.findAll();
        return tournaments;
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
