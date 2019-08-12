package org.pcp.tournament.service;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.model.Group;


public class GroupsStrategies {

    public static List<Group> createGroups(List<Team> teams, int groupNumber, GroupDao groupDao) {
        List<Group> groups = new ArrayList<Group>();        
        int baseTeamNumber = teams.size() / groupNumber;
        int remainingNumber = teams.size() % groupNumber;
        List<Integer> indexes = new ArrayList<Integer>();
        Random rnd = new Random();
        
        for (int i = 0; i < teams.size(); i++) {
            indexes.add(i);
        }
        try {
        for (int i = 0; i < groupNumber; i++) {
            Group group = new Group(i+"");            
            for (int j = 0; j < baseTeamNumber; j++) {
                int randomIndex = rnd.nextInt(indexes.size());
                int ti = indexes.get(randomIndex);
                indexes.remove(randomIndex);
                Team team = teams.get(ti);
                group.addTeam(team);
            }
            groupDao.save(group);
            groups.add(group);
        }
        for (int i = 0; i < remainingNumber; i++) {
            int randomIndex = rnd.nextInt(indexes.size());
            int ti = indexes.get(randomIndex);
            indexes.remove(randomIndex);
            Group group = groups.get(i);
            Team team = teams.get(ti);
            group.addTeam(team);
            groupDao.save(group);
        }
    }
    catch(Exception e) {
        System.out.println(e.getMessage());
    }

        return groups;
    }
}