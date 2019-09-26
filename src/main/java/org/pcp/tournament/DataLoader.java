package org.pcp.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
import org.pcp.tournament.dao.OptionsDao;
import org.pcp.tournament.dao.PlayerDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Group;
import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.Match;
import org.pcp.tournament.model.MatchSet;
import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.RunService;
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
    RunService runService;

    @Autowired
    MatchSetDao matchSetDao;

    @Autowired
    MatchDao matchDao;

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

//region faking    

    public Tournament buildFake(Tournament tournament, int count, int teamsByGroup) {
        List<Player> players = new ArrayList<Player>();
        int id = tournament.getId();
        for (int i = 0; i < count; i++) {
            Player pl = new Player(id+"l" + i, true);
            pl.setTournament(tournament);
            pl = playerDao.save(pl);
            if (tournament.getOptions().getMode() == Mode.DOUBLE) {
                Player pn = new Player(id+"n" + i, false);
                pn.setTournament(tournament);
                pn = playerDao.save(pn);
                players.add(pl);
                players.add(pn);
            }
        }
        tournament.setPlayers(players);
        tournament = tournamentDao.save(tournament);

        List<Team> teams = new ArrayList<Team>();
        for (int i = 0; i < count; i++) {
            Player p1 = playerDao.findByName(id+"l"+String.valueOf(i));
            Player p2 = null;
            if (tournament.getOptions().getMode() == Mode.DOUBLE) {
                p2 = playerDao.findByName(id+"n"+String.valueOf(i));
            }

            Team team = new Team(p1,p2);
            team.setTournament(tournament);
            team = teamDao.save(team);
            teams.add(team);
         }
         tournament.setTeams(teams);
         tournament = tournamentDao.save(tournament);

         List<Group> groups = new ArrayList<Group>();
         teams = tournament.getTeams();
         double d = (double)teams.size() / (double)teamsByGroup;
         double ceil = Math.ceil(d);
         int groupCount = (int)ceil;
         for (int i = 0 ; i < groupCount; i++ ) {
            String name = Character.toString ((char) (i+65));
            Group group = new Group(name);
            group = groupDao.save(group);

            for (int j = 0; j < teamsByGroup; j++) {
                group = addTeam(group,teams, i*teamsByGroup+j);
            }

            // group = addTeam(group,teams, i*4);
            // group = addTeam(group, teams, i*4+1);
            // group = addTeam(group, teams, i*4+2);
            // group = addTeam(group, teams, i*4+3);
            
            group.setTournament(tournament);
            group = groupDao.save(group);
            groups.add(group);
         }
         tournament.setGroups(groups);
         tournament = tournamentDao.save(tournament);
         return  tournament;
    }

    public Tournament buildFakeRun(Tournament tournament, boolean runGroups) {
        int id = tournament.getId();
        runService.buildGroupPhase(tournament);
        runService.buildMainBoard(tournament);
        runService.buildSecondBoard(tournament);
        tournament = tournamentDao.findById(id);
        if (runGroups) {
            for (int i = 0; i < tournament.getGroups().size(); i++) {
                playGroup(tournament, i);
            }
        }    
        tournament = tournamentDao.save(tournament);
        return tournament;       
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

    private void playMatch(Tournament tournament, Match match, int left, int right) {
        MatchSet set = match.getScore().get(0);
        set.setLeft(left);
        set.setRight(right);        
        set = matchSetDao.save(set);
        match.getScore().set(0, set);
        match = matchDao.save(match);
        match.compute(tournament.getOptions());
        runService.InjectTeams(tournament);
    }

//endregion    

    public Group addTeam(Group group,List<Team> teams, int index) {
        if (index < teams.size()) {
            Team t = teams.get(index);
            t.setGroup(group);
            t = teamDao.save(t);
            group.addTeam(t);
        }
        return group;
    }
}