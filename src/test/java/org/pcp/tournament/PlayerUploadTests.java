package org.pcp.tournament;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlayerUploadTests {

    @Autowired
    PlayersService playersService;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    OptionsDao optionsDao;

    @Test
    @Transactional
    public void testPlayersUpload() {
        try {
        Options options = optionsDao.findByMode(Mode.DOUBLE);
        Tournament tournament = new Tournament("testingrun");
        tournament.setOptions(options);
        tournament = tournamentDao.save(tournament);    
        ClassPathResource resource = new ClassPathResource("players.csv");    
        File csvFile = resource.getFile();
        playersService.ImportPlayers(tournament, csvFile);
        tournament = tournamentDao.findById(tournament.getId());
        List<Player> players = tournament.getPlayers();
        assertEquals(8, players.size());
        // TODO : check players
        }
        catch(Exception e) {
            fail("exception "+e.getMessage());
        }
    }

}