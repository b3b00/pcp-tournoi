package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.DataLoader;
import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Options;
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

    @Autowired
    GroupDao groupDao;

  

    @GetMapping(value = "/options/preset")
    public List<Options> Preset() {
        dataLoader.loadOptions();
        List<Options> options = optionsDao.findAllByIsPreset(true);

        return options;
    }

    @PostMapping(value = "/tournament/options")
    public int PostOptions(@RequestBody NameAndOptions nameAndOptions) {
        try {
            Options choosedOptions = nameAndOptions.getOptions();

            Options options = null;
            if (choosedOptions.getIsPreset() && choosedOptions.getId() > 0) {
                options = optionsDao.findById(choosedOptions.getId());
            } else {
                options = optionsDao.save(nameAndOptions.getOptions());
            }
            Tournament tournament = new Tournament(nameAndOptions.getName());
            tournament.setDate(nameAndOptions.getDate());
            tournament.setOptions(options);

            tournament = tournamentDao.save(tournament);
            tournament = dataLoader.buildFake(tournament, 16);

            return tournament.getId();

        } catch (Exception e) {
            throw new InternalError(e.getMessage());

        }
    }

   

    @PutMapping(value = "/tournament/{id}/options")
    public int PutOptions(@RequestBody NameAndOptions nameAndOptions, @PathVariable int id) {
        try {
            Tournament tournament = tournamentDao.findById(id);
            if (tournament != null) {
                tournament.setDate(nameAndOptions.getDate());
                tournament.setName(nameAndOptions.getName());
                Options options = tournament.getOptions();
                Options newOptions = nameAndOptions.getOptions();
                if (options.getIsPreset()) {
                    if (newOptions.getIsPreset() && newOptions.getId() != options.getId()) {
                        newOptions = optionsDao.findById(newOptions.getId());

                        tournament.setOptions(newOptions);
                    } else if (!newOptions.getIsPreset()) {
                        Options opts = new Options();
                        opts.setMode(newOptions.getMode());
                        opts.setWinningSets(newOptions.getWinningSets());
                        opts.setSetLength(newOptions.getSetLength());
                        opts = optionsDao.save(opts);
                        tournament.setOptions(opts);
                    }
                } else {
                    options.setMode(newOptions.getMode());
                    options.setWinningSets(newOptions.getWinningSets());
                    options.setSetLength(newOptions.getSetLength());
                    options = optionsDao.save(options);
                    tournament.setOptions(options);
                }
                Tournament tour = tournamentDao.save(tournament);
                return tour.getId();
            }
            return -1;

        } catch (Exception e) {
            throw new InternalError(e.getMessage());

        }
    }

}
