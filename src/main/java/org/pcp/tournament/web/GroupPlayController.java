package org.pcp.tournament.web;

import org.pcp.tournament.dao.GroupDao;
import org.pcp.tournament.dao.GroupPhaseDao;
import org.pcp.tournament.dao.GroupPlayDao;
import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.RunDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.model.TournamentBoard;
import org.pcp.tournament.model.dto.TeamRanking;
import org.pcp.tournament.service.MatchService;
import org.pcp.tournament.service.RunService;
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
    GroupPhaseDao groupPhaseDao;

    @Autowired
    MatchDao matchDao;

    @Autowired
    RunDao runDao;

    @Autowired
    MatchService matchService;

    @Autowired
    RunService runService;

    // region [GET]

    @GetMapping(value = "/groupPlay/{groupPlayId}")
    public GroupPlay getGroupPlay(@PathVariable int groupPlayId) {
        GroupPlay groupPlay = groupPlayDao.findById(groupPlayId);
        groupPlay.computeRanking();
        return groupPlay;
    }

    @GetMapping(value = "/groupPhase/{groupPhaseId}")
    public ResponseEntity<?> getGroupPhase(@PathVariable int groupPhaseId) {
        GroupPhase groupPhase = groupPhaseDao.findById(groupPhaseId);

        if (groupPhase != null) {
            for (GroupPlay group : groupPhase.getGroups()) {
                System.out.println("computing ranking for group " + group.getGroup().getName());
                group.computeRanking();
                System.out.println("ranking is ");

                for (TeamRanking rank : group.getRankings()) {
                    System.out.println(rank.toString());
                }
            }
            return new ResponseEntity<GroupPhase>(groupPhase, HttpStatus.OK);
        }
        return new ResponseEntity<String>("la phase de poule " + groupPhaseId + " n'existe pas.",
                HttpStatus.BAD_REQUEST);
    }

    // endregion

    // region [POST]

    @PostMapping("/tournaments/{tournamentId}/groupPhase/$create")
    public ResponseEntity<?> createGroupPhase(@PathVariable int tournamentId) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            try {
                

                tournament = runService.buildGroupPhase(tournament);
                tournament = tournamentDao.findById(tournamentId);
                TournamentBoard board = tournament.getRun().getBoard();
                
                if (board == null || board.getBoards() != null || board.getBoards().size() == 0) {
                    
                    runService.buildMainBoard(tournament);
                    runService.buildSecondBoard(tournament);
                    tournament = tournamentDao.findById(tournamentId);
                }

                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);

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
