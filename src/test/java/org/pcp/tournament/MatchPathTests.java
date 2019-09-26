package org.pcp.tournament;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchPathTests {


    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    RunService runService;

    @Autowired
    DBInitializer dbInitializer;

    @Autowired
    DataLoader dataLoader;

    @Autowired
    OptionsDao optionsDao;

    @Test    
    @Transactional
    public void dummyPassing() {
        System.out.println("dummy passing - in");

        Options options = optionsDao.findByMode(Mode.DOUBLE);
        Tournament tournament = new Tournament("testingrun");
        tournament.setOptions(options);
        tournament = tournamentDao.save(tournament);
        tournament = dataLoader.buildFake(tournament, 16,4);
        int id = tournament.getId();
        runService.buildGroupPhase(tournament);
        runService.buildMainBoard(tournament);

        tournament = tournamentDao.findById(id);

        assertNotNull(tournament.getRun());
        Run run = tournament.getRun();
        assertNotNull(run.getBoard());
        
        TournamentBoard board = run.getBoard();
        assertNotNull(board.getBoards());

        FinalPhase phase = board.getBoard("tableau principal");        
        assertNotNull(phase);
        assertEquals(3, phase.getRounds().size());
        Round round = phase.getRounds().get(0);
        assertEquals(4, round.getMatches().size());
        for (int i = 0; i < 4; i++) {
            Match match = round.getMatches().get(i);
            assertNotNull(match.getLeftTeamReference());
            assertNotNull(match.getRightTeamReference());
        }
        

        runService.InjectTeams(tournament);

        ;
    }

    
}