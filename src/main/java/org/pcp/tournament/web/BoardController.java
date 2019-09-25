package org.pcp.tournament.web;

import java.util.List;

import org.pcp.tournament.dao.FinalPhaseDao;
import org.pcp.tournament.dao.RoundDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.FinalPhase;
import org.pcp.tournament.model.Round;
import org.pcp.tournament.model.Team;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    RunService runService;

    @Autowired
    RoundDao roundDao;

    @Autowired
    FinalPhaseDao boardDao;

    @PostMapping("/tournaments/{tournamentId}/board/$create")
    public ResponseEntity<?> createGroupPhase(@PathVariable int tournamentId) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            if (tournament != null) {
                runService.buildMainBoard(tournament);
                runService.buildSecondBoard(tournament);
                tournament = tournamentDao.findById(tournamentId);

                return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
            }
            return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("hello " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tournament/{tournamentId}/round/{roundId}")
    public ResponseEntity<?> getRound(@PathVariable int tournamentId, @PathVariable int roundId) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            runService.InjectTeams(tournament);
            runService.computeTeamReferenceLabels(tournament);
            Round round = roundDao.findById(roundId);
            if (round != null) {
                round.getMatches().stream().forEach(m -> {
                    m.compute(tournament.getOptions());
                });
                return new ResponseEntity<Round>(round, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("round not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("hello " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tournament/{tournamentId}/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable int tournamentId, @PathVariable int boardId) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            runService.computeTeamReferenceLabels(tournament);
            runService.InjectTeams(tournament);
            runService.computeTeamReferenceLabels(tournament);
            FinalPhase board = boardDao.findById(boardId);
            if (board != null) {
                // board.getRounds().stream().forEach(m -> {
                // m.computeScores(tournament.getOptions());
                // });
                return new ResponseEntity<FinalPhase>(board, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("board not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tournament/{tournamentId}/availableTeams")
    public ResponseEntity<?> getAvailableTeams(@PathVariable int tournamentId) {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            List<Team> teams = runService.getAvailableTeams(tournament);
            return new ResponseEntity<List<Team>>(teams, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @PostMapping("/tournament/{tournamentId}/$createBoard")
    public ResponseEntity<?> createBoard(@PathVariable int tournamentId,@RequestBody List<Integer> teamsId,@RequestParam("name") String boardName)
    {
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            tournament = runService.buildBoardWithTeams(tournament, teamsId ,boardName);
            return new ResponseEntity<Tournament>(tournament, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
