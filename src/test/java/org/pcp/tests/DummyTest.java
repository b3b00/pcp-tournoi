package org.pcp.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pcp.tournament.dao.*;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.MyComp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// @ComponentScan(basePackages = {"org.pcp.tournament.dao","org.pcp.tournament.service.*"})
public class DummyTest {

    @Autowired
    MyComp myComp;

    // @BeforeClass
    // public static void beforeClass() {        
    //     System.out.println("before class");
    // }

    // @Before
    // public void before() {
    //     System.out.println("before");
    // }

    @Test
    public void dummyPassing() {
        System.out.println("dummy passing - in");

        myComp.testMe();

        // java.util.List<Options> options = optionsDao.findAll();
        // for (Options option : options) {
        //     System.out.println(option.getId() + " - " + option.toString());
        // }
        assertTrue("passing test ", true);
        System.out.println("dummy passing - out");
    }

    
}