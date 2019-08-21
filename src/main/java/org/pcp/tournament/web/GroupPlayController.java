package org.pcp.tournament.web;

import java.util.ArrayList;
import java.util.List;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.GroupPlayDao;
import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Group;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.Match;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupPlayController {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    GroupPlayDao groupPlayDao;

    @Autowired
    MatchDao matchDao;

    @Autowired
    MatchService matchService;

    // region [GET]

    @GetMapping(value = "/groupPlay/{groupPlayId}")
    public GroupPlay getPGroupPlay(@PathVariable int groupPlayId) {
        GroupPlay groupPlay = groupPlayDao.findById(groupPlayId);
        return groupPlay;
    }

    // TODO get group phase ?

    // endregion

    // region [POST]

    @PostMapping("/tournaments/{tournamentId}/groupPlays/$create")
    public ResponseEntity<?> createGroupPlays(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            try {
                Options options = tournament.getOptions();
                List<GroupPlay> groupPlays = new ArrayList<GroupPlay>();

                for (Group group : tournament.getGroups()) {
                    GroupPlay play = new GroupPlay();
                    play.setGroup(group);

                    List<Team> teams = group.getTeams();
                    List<Match> matches = new ArrayList<Match>();

                    int len = teams.size();
                    for (int i = len - 1; i >= 0; i--) {
                        for (int j = i - 1; j >= 0; j--) {
                            Team t1 = teams.get(i);
                            Team t2 = teams.get(j);
                            Match match = new Match();
                            match.setLeft(t1);
                            match.setRight(t2);
                            Match newMatch = matchService.createMatch(match, options);
                            matches.add(newMatch);
                        }
                    }
                    play.setMatches(matches);
                    groupPlayDao.save(play);

                }
                return new ResponseEntity<List<GroupPlay>>(groupPlays, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<String>("error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    }

    // region [PUT]

    // endregion

    // region [DELETE]

    // endregion

}
