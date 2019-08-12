package org.pcp.tournament.web;

import java.util.ArrayList;
import java.util.List;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.TeamDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Group;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.GroupsStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsController {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    TeamDao teamDao;

    @GetMapping(value = "/tournaments/{tournamentId}/groups")
    public List<Group> getAll(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        List<Group> groups = tournament.getGroups();
        return groups;
    }

    private Tournament clearGroups(Tournament tournament) {
        List<Group> groups = new ArrayList<Group>();
        groups.addAll(tournament.getGroups());
        
        tournament.setGroups(new ArrayList<Group>());
        tournament = tournamentDao.save(tournament);
        
        for (Group group : groups) {
            group.getTeams().clear();
            groupDao.save(group);
            groupDao.delete(group);
        }
        return tournament;
    }

    @PostMapping("/tournaments/{tournamentId}/groups/{number}")
    public ResponseEntity<?> createGroups(@PathVariable int tournamentId, @PathVariable int number) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            if (!tournament.getGroups().isEmpty()) {
                clearGroups(tournament);
            }
            List<Group> groups = GroupsStrategies.createGroups(tournament.getTeams(),number,groupDao);
            tournament.setGroups(groups);
            tournament =  tournamentDao.save(tournament);
            return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("tournaments/{tournamentId}/groups")
    public ResponseEntity<?> deleteGroups(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            
            clearGroups(tournament);            
            tournament =  tournamentDao.save(tournament);
            return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

}
