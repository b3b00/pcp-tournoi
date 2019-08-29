package org.pcp.tournament;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.dao.*;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.TeamStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchPathTests {


  

    @Autowired
    DBInitializer dbInitializer;

    @Test
    public void dummyPassing() {
        System.out.println("dummy passing - in");
        Tournament tournament = dbInitializer.InitTournament();        
        assertTrue("passing test ", true);
        System.out.println("dummy passing - out");
    }

    
}