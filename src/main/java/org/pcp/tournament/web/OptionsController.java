package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.dao.OptionsDao;
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

    @GetMapping(value = "/options/preset")
    public List<Options> Preset() {
        List<Options> options =  optionsDao.findAllByIsPreset(true);
        return options;
    }

    @PostMapping(value="/tournament/options")
    public void PostOptions(@RequestBody NameAndOptions nameAndOptions) {
        Options options = optionsDao.save(nameAndOptions.getOptions());
        Tournament tournament = new Tournament(nameAndOptions.getName());
        tournament.setOptions(options);
        tournamentDao.save(tournament);
        ;
    }

}
