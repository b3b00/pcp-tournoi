package org.pcp.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Group;
import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
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
    TournamentDao tournamentDao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    public DataLoader(OptionsDao optionsDao) {
        this.optionsDao = optionsDao;
        loadOptions();
    }

    public void loadOptions() {
        List<Options> options = optionsDao.findAllByIsPreset(true);
        if (options == null || options.isEmpty()) {
            Options single = new Options(Mode.SINGLE,"simple", 3,11,true);
            optionsDao.save(single);
            Options doubl = new Options(Mode.DOUBLE,"double", 1,50,true);
            optionsDao.save(doubl);
        }
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

    public Tournament buildFake(Tournament tournament, int count) {
        List<Player> players = new ArrayList<Player>();
        int id = tournament.getId();
        for (int i = 0; i < count; i++) {
            Player pl = new Player(id+"l" + i, true);
            pl.setTournament(tournament);
            pl = playerDao.save(pl);
            Player pn = new Player(id+"n" + i, false);
            pn.setTournament(tournament);
            pn = playerDao.save(pn);
            players.add(pl);
            players.add(pn);
        }
        tournament.setPlayers(players);
        tournament = tournamentDao.save(tournament);

        List<Team> teams = new ArrayList<Team>();
        for (int i = 0; i < count; i++) {
            Player p1 = playerDao.findByName(id+"l"+String.valueOf(i));
            Player p2 = playerDao.findByName(id+"n"+String.valueOf(i));

            Team team = new Team(p1,p2);
            team.setTournament(tournament);
            team = teamDao.save(team);
            teams.add(team);
         }
         tournament.setTeams(teams);
         tournament = tournamentDao.save(tournament);

         List<Group> groups = new ArrayList<Group>();
         teams = tournament.getTeams();
         for (int i = 0 ; i < count/4; i++ ) {
            String name = Character.toString ((char) (i+65));
            Group group = new Group(name);
            group = groupDao.save(group);
            Team t1 = teams.get(i*4);
            t1.setGroup(group);
            t1 = teamDao.save(t1);
            Team t2 = teams.get(i*4+1);
            t2.setGroup(group);
            t2 = teamDao.save(t2);
            Team t3 = teams.get(i*4+2);
            t3.setGroup(group);
            t3 = teamDao.save(t3);
            Team t4 = teams.get(i*4+3);
            t4.setGroup(group);
            t4 = teamDao.save(t4);
            group.addTeam(t1);
            group.addTeam(t2);
            group.addTeam(t3);
            group.addTeam(t4);
            group.setTournament(tournament);
            group = groupDao.save(group);
            groups.add(group);
         }
         tournament.setGroups(groups);
         tournament = tournamentDao.save(tournament);
         return  tournament;
    }
}