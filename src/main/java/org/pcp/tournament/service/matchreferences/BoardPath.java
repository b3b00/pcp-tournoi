package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.FinalPhase;
import org.pcp.tournament.model.IPingModel;
import org.pcp.tournament.model.TournamentBoard;


public class BoardPath implements IMatchPath  {

    private String name;

    public BoardPath(String name) {
        this.name = name;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof TournamentBoard) {
            TournamentBoard boards = (TournamentBoard)model;  
            FinalPhase board = boards.getBoard(name);
            if (board != null) {
                return board;
            }
            else {
                throw new MatchPathException("board "+name+" not found");
            }
        }
        else {
            throw new MatchPathException("expecting a group phase , found "+model.getClass().getName());
        }
    }
}


