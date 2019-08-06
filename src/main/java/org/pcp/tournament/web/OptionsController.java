package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.DataLoader;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.model.dto.NameAndOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {

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

    @GetMapping(value = "/options/preset")
    public List<Options> Preset() {
        List<Options> options = optionsDao.findAllByIsPreset(true);
        return options;
    }


   

    @PostMapping(value = "/tournament/options")
    public int PostOptions(@RequestBody NameAndOptions nameAndOptions) {
        try {
            Options options = optionsDao.save(nameAndOptions.getOptions());
            Tournament tournament = new Tournament(nameAndOptions.getName());
            tournament.setOptions(options);
            Tournament tour = tournamentDao.save(tournament);
            dataLoader.buildPlayers(16,tour.getId());
            tour.addPlayers(playerDao.findAll());
            dataLoader.buildTeams();
            tour.addTeams(teamDao.findAll());
            tournamentDao.save(tour);

            
            return tour.getId();

        } catch (Exception e) {
            throw new InternalError(e.getMessage());

        }
    }

}
