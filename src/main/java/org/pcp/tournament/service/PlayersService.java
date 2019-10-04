package org.pcp.tournament.service;

import java.io.File;
import java.nio.file.Paths;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayersService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TournamentDao tournamentDao;

    public void ImportPlayers(Tournament tournament, String csvContent) throws Exception {

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
                String name = nextRecord[0];
                boolean licensee = false;
                if (nextRecord.length > 1) {
                    String lic = nextRecord[1];
                    licensee = lic != null && (lic.equals("*") ||lic.equals("+"));
                }
                Player player = new Player(name,licensee);
                player.setTournament(tournament);
                player = playerDao.save(player);
                tournament.addPlayer(player);

                tournament = tournamentDao.save(tournament);
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

    public void ImportPlayers(Tournament tournament, File csvFile) throws Exception {
            byte[] bytes = Files.readAllBytes(Paths.get(csvFile.getPath()));
            ImportPlayers(tournament, new String(bytes));
    }

    public String playersToCSV(Tournament tournament) {
        StringBuilder builder = new StringBuilder();
        for (Player player : tournament.getPlayers()) {
            if (player != null) {
                builder.append(player.getName());
                builder.append(';');
                if (player.getIsLicensed()) {
                    builder.append("*");
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }

}