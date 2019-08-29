package org.pcp.tournament;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.dao.*;
import org.pcp.tournament.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DummyTest {


    @Autowired
    OptionsDao optionsDao;

    @Autowired
    PlayerDao playerDao;

    @Test
    public void dummyPassing() {
        System.out.println("dummy passing - in");
        List<Options> options = optionsDao.findAll();
        for (Options option : options) {
            System.out.println(option.toString());
        }
        List<Player> players = playerDao.findAll();

        assertTrue("passing test ", true);
        System.out.println("dummy passing - out");
    }

    
}