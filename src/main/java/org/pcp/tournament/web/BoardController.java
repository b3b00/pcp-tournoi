package org.pcp.tournament.web;

import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    RunService runService;

    

    @PostMapping("/tournaments/{tournamentId}/board/$create")
    public ResponseEntity<?> createGroupPhase(@PathVariable int tournamentId,@RequestParam("start")int startingRound) {
        Tournament tournament = tournamentDao.findById(tournamentId);
        if (tournament != null) {
            runService.buildBoard(tournament, startingRound);
        }
        return new ResponseEntity<String>("le tournoi " + tournamentId + " n'existe pas.", HttpStatus.BAD_REQUEST);
    } 

}
