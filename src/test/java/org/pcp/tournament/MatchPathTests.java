package org.pcp.tournament;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import org.junit.Test;

import org.junit.runner.RunWith;
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

    @Test    
    @Transactional
    public void dummyPassing() {
        System.out.println("dummy passing - in");
        Tournament tournament = dbInitializer.InitTournament();        
        assertNotNull(tournament);
        tournament = runService.buildGroupPhase(tournament);
        runService.buildBoard(tournament, 8);
        tournament = tournamentDao.findById(tournament.getId());
        assertNotNull(tournament.getRun());
        Run run = tournament.getRun();
        assertNotNull(run.getBoard());
        TournamentBoard board = run.getBoard();
        assertNotNull(board.getBoards());
        FinalPhase phase = board.getBoard("I");
        Round round = phase.getRounds().get(0);
        for (int i = 0; i < 8; i++) {
            Match match = round.getMatches().get(i);
            System.out.println(match.getLeftTeamReferenceLabel()+ " - "+match.getRightTeamReferenceLabel());
        }
        assertNotNull(phase);
        assertEquals(4, phase.getRounds().size());
        ;
    }

    
}