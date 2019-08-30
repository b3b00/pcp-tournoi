package org.pcp.tournament;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchPathTests {


  

    @Autowired
    DBInitializer dbInitializer;

    @Test    
    @Transactional
    public void dummyPassing() {
        System.out.println("dummy passing - in");
        Tournament tournament = dbInitializer.InitTournament();        
        assertNotNull(tournament);
        System.out.println("dummy passing - out");
    }

    
}