package org.pcp.tournament.service;

import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.model.Player;   
import org.pcp.tournament.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TeamStrategies {

    /**
     * hasard total : 2 joueurs pris au hasard sans autre critères
     * @param players
     * @param teamDao
     * @return
     */


    public static List<Team> single(List<Player> players, TeamDao teamDao) {
        List<Team> teams = new ArrayList<Team>();
        
        for (Player player : players) {
            Team team = new Team(player, null);
            teamDao.save(team);
            teams.add(team);            
        }
        return teams;
    }

    public static List<Team> pureRandom(List<Player> players, TeamDao teamDao) {
        Random rnd = new Random();
        List<Team> teams = new ArrayList<Team>();
        
        int count = players.size();
        List<Integer> playersIds = players.stream().map((Player p) -> p.getId()).collect(Collectors.toList());
        for (int i = 0; i< count / 2 ; i++) {
            int f = rnd.nextInt(playersIds.size());
            int p1Id = playersIds.get(f);            
            playersIds.remove(f);
            Player p1 = players.stream().filter(p -> p.getId() == p1Id).findFirst().get();

            int s = rnd.nextInt(playersIds.size());
            int p2Id = playersIds.get(s);
            playersIds.remove(s);
            Player p2 = players.stream().filter(p -> p.getId() == p2Id).findFirst().get();

            Team team = new Team(p1,p2);

            teamDao.save(team);
            teams.add(team);
        }
        return teams;
    }

    

    /**
     * on commence par faire des paires avec 1 licencié et 1 non licencié
     * jsuqu'à épuisement d'1 des 2 groupes 
     * ensuite equipes non mixtes
     * @param players
     * @param teamDao
     * @return
     */
    public static List<Team> mixLicensees(List<Player> players, TeamDao teamDao) {
        List<Player> licensees = players.stream().filter((Player p) -> p.getIsLicensed()).collect(Collectors.toList());
        List<Player> notLicensees = players.stream().filter((Player p) -> !p.getIsLicensed()).collect(Collectors.toList());

        int countMix = Math.min(licensees.size(),notLicensees.size());
        List<Team> teams = new ArrayList<Team>();
        Random rnd = new Random();
        for (int i = 0; i < countMix; i++) {
            int f = rnd.nextInt(licensees.size());
            Player p1 = licensees.get(f);
            licensees.remove(f);

            int s = rnd.nextInt(notLicensees.size());
            Player p2 = notLicensees.get(s);
            notLicensees.remove(s);

            Team team = new Team(p1,p2);

            teamDao.save(team);
            teams.add(team);
        }

        if (licensees.size() > 0) {
            List<Team> licenseeTeams = pureRandom(licensees, teamDao);
            teams.addAll(licenseeTeams);
        }
        else if(notLicensees.size() > 0) {
            List<Team> notLicenseeTeams = pureRandom(notLicensees, teamDao);
            teams.addAll(notLicenseeTeams);
        }
        
        return teams;
    }
}