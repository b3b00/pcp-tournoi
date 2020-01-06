package org.pcp.tournament;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.PlayersService;
import org.pcp.tournament.service.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamUploadTests {

    @Autowired
    TeamsService teamsService;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    OptionsDao optionsDao;

    @Autowired    
    TeamDao teamDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    PlayersService playersService;

    @Test
    @Transactional
    public void testTeamsUpload() {
        try {
            Options options = optionsDao.findByMode(Mode.DOUBLE);
            Tournament tournament = new Tournament("testingrun");
            tournament.setOptions(options);
            tournament = tournamentDao.save(tournament);    
            ClassPathResource resource = new ClassPathResource("players.csv");    
            File csvFile = resource.getFile();
            playersService.ImportPlayers(tournament, csvFile); 
            resource = new ClassPathResource("teams.csv");    
            csvFile = resource.getFile();
            teamsService.importTeams(tournament, csvFile);
            tournament = tournamentDao.findById(tournament.getId());
            List<Team> teams = tournament.getTeams();
            assertEquals(4, teams.size());
        // TODO : check players
        }
        catch(Exception e) {
            fail("exception "+e.getMessage());
        }
    }

    @Test
    @Transactional
    public void testPlayersDownload() {
        try {
            Options options = optionsDao.findByMode(Mode.DOUBLE);
            Tournament tournament = new Tournament("testingrun");
            tournament.setOptions(options);
            tournament = tournamentDao.save(tournament);    
            ClassPathResource resource = new ClassPathResource("players.csv");    
            File csvFile = resource.getFile();
            playersService.ImportPlayers(tournament, csvFile); 
            tournament = tournamentDao.save(tournament);

            List<Player> players = tournament.getUnteamedPlayers();
            assertEquals(8, players.size());

            Team team = new Team(players.get(0),players.get(1));
            team = teamDao.save(team);
            tournament.addTeam(team);
            tournament = tournamentDao.save(tournament);

            team = new Team(players.get(2),players.get(3));
            team = teamDao.save(team);
            tournament.addTeam(team);
            tournament = tournamentDao.save(tournament);

            String csv = teamsService.teamsToCSV(tournament);

            Reader reader = new StringReader(csv);

            CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

            CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build();

            List<String[]> lines = csvReader.readAll();
            assertEquals(2, lines.size());
            String[] l1 = lines.get(0); 
            assertEquals(2, l1.length);
            assertEquals("Juju", l1[0]);
            assertEquals("Marcel", l1[1]);
            String[] l2 = lines.get(1); 
            assertEquals(2, l2.length);
            assertEquals("Fred", l2[0]);
            assertEquals("Yann", l2[1]);

        }
        catch(Exception e) {
            fail(e.getMessage());
        }

    }
}