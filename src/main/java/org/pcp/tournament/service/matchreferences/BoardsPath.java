package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.IPingModel;
import org.pcp.tournament.model.Tournament;

public class BoardsPath implements IMatchPath  {


    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof Tournament) {
            Tournament tournament = (Tournament)model;  
            return tournament.getRun().getBoard();
        }
        else {
            throw new MatchPathException("expecting a group phase , found "+model.getClass().getName());
        }
    }
}

