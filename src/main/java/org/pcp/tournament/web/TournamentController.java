package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.DataLoader;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
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
    DataLoader dataLoader;

    @GetMapping(value = "/tournaments")
    public List<Tournament> Preset() {
        List<Tournament> tournaments = tournamentDao.findAll();
        return tournaments;
    }


}
