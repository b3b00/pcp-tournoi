package org.pcp.tournament;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
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
public class MatchRunTests {


    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    DataLoader dataLoader;

    @Autowired
    OptionsDao optionsDao;

    @Autowired
    RunService runService;

    @Autowired
    MatchSetDao matchSetDao;

    @Autowired
    MatchDao matchDao;

    @Test
    @Transactional
    public void testTournamentDelete() {
        Options options = optionsDao.findByMode(Mode.DOUBLE);
        Tournament tournament = new Tournament("testingrun");
        tournament.setOptions(options);
        tournament = tournamentDao.save(tournament);
        tournament = dataLoader.buildFake(tournament, 16);
        int id = tournament.getId();

        tournamentDao.deleteById(id);

        tournament = tournamentDao.findById(id);
        assertNull(tournament);
    }

    @Test    
    @Transactional
    public void testingRun() {
        Options options = optionsDao.findByMode(Mode.DOUBLE);
        Tournament tournament = new Tournament("testingrun");
        tournament.setOptions(options);
        tournament = tournamentDao.save(tournament);
        tournament = dataLoader.buildFake(tournament, 16);
        int id = tournament.getId();
        runService.buildGroupPhase(tournament);
        runService.buildMainBoard(tournament);
        runService.buildSecondBoard(tournament);
        tournament = tournamentDao.findById(id);
        for (int i = 0; i < 4; i++) {
            playGroup(tournament, i);
        }
        checkRound(tournament,"tableau principal", 0);
        playRound(tournament,"tableau principal", 0);

        checkRound(tournament,"tableau principal", 1);
        playRound(tournament,"tableau principal", 1);

        // checkRound(tournament,"tableau principal", 2);
        // playRound(tournament,"tableau principal", 2);

        FinalPhase board = tournament.getRun().getBoard().getBoards().get(0);
        Round round = board.getRounds().get(1);

        List<Match> matches = round.getMatches();
        assertEquals(2, matches.size());
        
        Match finale = matches.stream().filter(m -> m.isFinale()).findFirst().get();
        assertNotNull(finale);
        assertTrue("finale ended", finale.getIsEnded());
        Team winner = finale.getWinner();

        assertTrue("winner check", winner.getPlayer1().getName().endsWith("l1"));
        //assertEquals("3l5", winner.getPlayer1().getName());

        Match smallfinale = matches.stream().filter(m -> m.isSemiFinale()).findFirst().get();
        assertNotNull(smallfinale);
        assertTrue("finale ended", smallfinale.getIsEnded());
        Team smallWinner = smallfinale.getWinner();
        assertTrue("small winner check", smallWinner.getPlayer1().getName().endsWith("l4"));

        checkRound(tournament,"consolante", 0);
        playRound(tournament,"consolante", 0);

        checkRound(tournament,"consolante", 1);
        playRound(tournament,"consolante", 1);

        FinalPhase consolante = tournament.getRun().getBoard().getBoard("consolante");
        int roundCount = consolante.getRounds().size();
        round = consolante.getRounds().get(roundCount-1);
        Match consollingFinal = round.getMatches().stream().filter(m -> m.isFinale()).collect(Collectors.toList()).get(0);
        assertTrue("finale ended", smallfinale.getIsEnded());
        String winnerName = consollingFinal.getWinner().getName();
        System.out.println("winner "+winnerName);
        Player p1 = consollingFinal.getWinner().getPlayer1();
        assertTrue("winner check", p1.getName().endsWith("l3")); 
              
        
    }

    private void playGroup(Tournament tournament, int groupNumber) {
        GroupPhase groups = tournament.getRun().getGroupPhase();
        GroupPlay group = groups.getGroups().get(groupNumber);
        List<Match> matches = group.getMatches();
        for (Match match : matches) {
            playMatch(tournament, match, 0, 50);
        }
        group.computeRanking();
    }

    private void playRound(Tournament tournament, String boardName, int roundNumber) {
        FinalPhase board = tournament.getRun().getBoard().getBoard(boardName);
        Round round = board.getRounds().get(roundNumber);
        for (Match match : round.getMatches()) {
            playMatch(tournament, match, 0, 50);            
        }
    }

    private void checkRound(Tournament tournament,String boardName, int roundNumber) {
        FinalPhase board = tournament.getRun().getBoard().getBoard(boardName);
        Round round = board.getRounds().get(roundNumber);
        for (Match match : round.getMatches()) {
            assertNotNull(match.getLeft());
            assertNotNull(match.getRight());
        }
    }

    private void playMatch(Tournament tournament, Match match, int left, int right) {
        MatchSet set = match.getScore().get(0);
        set.setLeft(left);
        set.setRight(right);
        set = matchSetDao.save(set);
        match.getScore().set(0,set);
        match = matchDao.save(match);
        runService.InjectTeams(tournament);
    }

}