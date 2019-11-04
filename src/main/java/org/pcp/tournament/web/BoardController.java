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
import org.pcp.tournament.web.exception.PCPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class BoardController extends PCPController {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    RunService runService;

    @Autowired
    RoundDao roundDao;

    @Autowired
    FinalPhaseDao boardDao;

    
    @ApiOperation(value = "create the group pahse of a tournament", response = Tournament.class)
    @PostMapping("/tournaments/{tournamentId}/board/$create")    
    public ResponseEntity<?> createGroupPhase(@PathVariable int tournamentId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
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

    @ApiOperation(value = "get a round from a board", response = Round.class)  
    @GetMapping("/tournament/{tournamentId}/round/{roundId}")
    public ResponseEntity<?> getRound(@ApiParam("tournament id")@PathVariable int tournamentId, @ApiParam("round id")@PathVariable int roundId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
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

    @ApiOperation(value = "get a board from a tournament", response = Round.class)  
    @GetMapping("/tournament/{tournamentId}/board/{boardId}")
    public ResponseEntity<?> getBoard(@ApiParam("tournament id")@PathVariable int tournamentId, @ApiParam("board id")@PathVariable int boardId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            runService.computeTeamReferenceLabels(tournament);
            runService.InjectTeams(tournament);
            runService.computeTeamReferenceLabels(tournament);
            FinalPhase board = boardDao.findById(boardId);
            if (board != null) {
                return new ResponseEntity<FinalPhase>(board, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("board not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "get available teams", response = Round.class)  
    @GetMapping("/tournament/{tournamentId}/availableTeams")
    public ResponseEntity<?> getAvailableTeams(@ApiParam("tournament ID")@PathVariable int tournamentId, final Authentication authentication) throws PCPException {
        checkIdentity(authentication,tournamentId);
        try {
            Tournament tournament = tournamentDao.findById(tournamentId);
            List<Team> teams = runService.getAvailableTeams(tournament);
            return new ResponseEntity<List<Team>>(teams, HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @ApiOperation(value = "create a board", response = Tournament.class)  
    @PostMapping("/tournament/{tournamentId}/$createBoard")
    public ResponseEntity<?> createBoard(@ApiParam("tournament ID")@PathVariable int tournamentId,@ApiParam("a list of teams id")@RequestBody List<Integer> teamsId,@ApiParam("board name")@RequestParam("name") String boardName, final Authentication authentication) throws PCPException
    {
        checkIdentity(authentication,tournamentId);
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
