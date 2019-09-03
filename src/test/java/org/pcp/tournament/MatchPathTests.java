package org.pcp.tournament;

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
        runService.buildBoard(tournament, 4);
        tournament = tournamentDao.findById(tournament.getId());
        
        ;
    }

    
}