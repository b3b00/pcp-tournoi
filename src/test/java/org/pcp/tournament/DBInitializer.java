package org.pcp.tournament;

import java.util.List;

import org.pcp.tournament.dao.*;
import org.pcp.tournament.model.*;
import org.pcp.tournament.service.GroupsStrategies;
import org.pcp.tournament.service.TeamStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer {
    
    @Autowired
    OptionsDao optionsDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    TeamStrategies teamStrateg;

    @Autowired
    GroupsStrategies groupStrateg;

    public Tournament InitTournament() {
        Tournament tournament = tournamentDao.findById(0);
        List<Player> players = tournament.getPlayers();
        List<Team> teams = teamStrateg.single(players, tournament);
        tournament.setTeams(teams);
        tournament = tournamentDao.save(tournament);
        List<Group> groups = groupStrateg.createGroups(tournament, 8);
        tournament.setGroups(groups);
        tournament = tournamentDao.save(tournament);
        return tournament;
    }

   

    

}