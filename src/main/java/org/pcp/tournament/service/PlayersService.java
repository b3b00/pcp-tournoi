package org.pcp.tournament.service;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.CSVReader;

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

    public void ImportPlayers(Tournament tournament, String csv) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csv));
            CSVReader csvReader = new CSVReader(reader);
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                String name = nextRecord[0];
                boolean licensee = false;
                if (nextRecord.length > 1) {
                    String lic = nextRecord[1];
                    licensee = lic != null && (lic.equals("*") ||lic.equals("+"));
                }
                Player player = new Player(name,licensee);
                player = playerDao.save(player);
                tournament.addPlayer(player);
                tournament = tournamentDao.save(tournament);
            }
        }
        catch (Exception e) {
            // TODO !
        }
    }

}