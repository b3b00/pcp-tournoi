package org.pcp.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private OptionsDao optionsDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TeamDao teamDao;

    @Autowired
    public DataLoader(OptionsDao optionsDao) {
        this.optionsDao = optionsDao;
        loadOptions();
    }

    public void loadOptions() {
        Options single = new Options(Mode.SINGLE,3,11,true);
        optionsDao.save(single);
        Options doubl = new Options(Mode.DOUBLE,1,50,true);
        optionsDao.save(doubl);
    }

    public void buildPlayers(int count,int id) {
        List<Player> players = new ArrayList<Player>();
            for (int i = 0; i < count; i++) {
                Player player = new Player("player #" + i+" / "+id, i % 2 == 0);
                players.add(player);
                playerDao.save(player);
            }
    }

    public void buildTeams() {
        List<Player> players = playerDao.findAll();

        Random rnd = new Random();
        int count = players.size();
        for (int i = 0; i< count / 2 ; i++) {
            int f = rnd.nextInt(players.size());
            Player p1 = players.get(f);
            players.remove(f);
            int s = rnd.nextInt(players.size());
            Player p2 = players.get(s);
            players.remove(s);

            Team team = new Team(p1,p2);
            teamDao.save(team);

        }
    }
}