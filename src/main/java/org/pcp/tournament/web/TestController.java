package org.pcp.tournament.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.TeamGroup;
import org.pcp.tournament.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @Autowired
    GroupDao groupDao;


    @Autowired
    PlayerDao playerDao;

    @Autowired
    TeamDao teamDao;

    @Autowired
    OptionsDao optionsDao;

    @Autowired
    TournamentDao tournamentDao;

    @GetMapping(value = "/hello")
    public String Hello() {
        return "<h1>HELLO</h1>";
    }


    private void buildPlayers(int count) {
        List<Player> players = new ArrayList<Player>();
            for (int i = 0; i < count; i++) {
                Player player = new Player("player #" + i, i % 2 == 0);
                players.add(player);
                playerDao.save(player);
            }
    }

    private void buildTeams() {
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

    @GetMapping(value = "/players")
    public String Players() {
        buildPlayers(10);

        List<Player> players = playerDao.findAll();
        String content = "<h1>Players : </h1>";
        for (Player player : players) {
            content += player.toString() + "<br>";
        }
        return content;
    }

    @GetMapping(value = "/teams")
    public String Teams() {
        int count = 2*6;
        buildPlayers(count);

        List<Player> players = playerDao.findAll();

        buildTeams();

        List<Team> teams = teamDao.findAll();

        String content = "<h1>teams : </h1>";
        for (Team team : teams) {
            content += team.toString() + "<br>";
        }
        return content;
    }

    private void buildGroups() {
        List<TeamGroup> groups = new ArrayList<TeamGroup>();
        List<Team> teams = teamDao.findAll();
        for (int i =0 ; i < teams.size()/2;i++) {
            TeamGroup group = new  TeamGroup("#"+i);            
            group.AddTeam(teams.get(i*2));
            group.AddTeam(teams.get(i*2+1));
            groupDao.save(group);
        }
    }

    private void initModes( ) {
        Options single = new Options(Mode.SINGLE,3,11);
        optionsDao.save(single);
        Options doubl = new Options(Mode.DOUBLE,1,50);
        optionsDao.save(doubl);
    }

    @GetMapping("/tour/{c}")
    public void tournament(@PathVariable int c) {
        buildPlayers(c);
        List<Player> players = playerDao.findAll();
        buildTeams();
        List<Team> teams = teamDao.findAll();
        buildGroups();

        Tournament tour = new Tournament("tournoi du PCP");
        tournamentDao.save(tour);
        initModes();
        List<Options> optionsList = optionsDao.findAll();
        Options options = optionsList.get(1);
        options = optionsDao.findByMode(Mode.DOUBLE);
        List<TeamGroup> groups = groupDao.findAll();
        tour.setOptions(options);
        tour.setPlayers(players);
        tour.setTeams(teams);
        tour.setGroups(groups);
        tournamentDao.save(tour);
    }

}
