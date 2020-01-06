package org.pcp.tournament.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class TeamsService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TeamDao teamDao;


    @Autowired
    TournamentDao tournamentDao;

    public void importTeams(Tournament tournament, String csvContent) throws Exception {

        CSVReader csvReader = null;
        try {
            Reader reader = new StringReader(csvContent);

            CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

            csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build();

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord.length == 2) {
                    String player1Name = nextRecord[0];
                    String player2Name = nextRecord[1];
                    Optional<Player> player1 = tournament.getPlayers().stream().filter(p ->  p.getName().equals(player1Name)).findFirst();
                    Optional<Player> player2 = tournament.getPlayers().stream().filter(p ->  p.getName().equals(player2Name)).findFirst();

                    if (player1.isPresent() && player2.isPresent()) {
                        Team team = new Team(player1.get(),player2.get());
                        tournament.addTeam(team);
                        team.setTournament(tournament);
                        team = teamDao.save(team);
                    }

                    tournament = tournamentDao.save(tournament);
                }
            }
            csvReader.close();
        }        
        catch (Exception e) {
            throw e;
        }
        finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                }
                catch(Exception e) {
                    throw e;
                }
            }
        } 
    }

    public void importTeams(Tournament tournament, File csvFile) throws Exception {
            byte[] bytes = Files.readAllBytes(Paths.get(csvFile.getPath()));
            importTeams(tournament, new String(bytes));
    }

    public String teamsToCSV(Tournament tournament) {
        StringBuilder builder = new StringBuilder();
        for (Team team : tournament.getTeams()) {
            if (team != null) {
                builder.append(team.getPlayer1().getName());
                builder.append(';');
                builder.append(team.getPlayer2().getName());
                builder.append("\n");
            }
        }
        return builder.toString();
    }

}