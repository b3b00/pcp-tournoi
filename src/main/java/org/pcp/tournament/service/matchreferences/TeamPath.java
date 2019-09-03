package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.*;


public class TeamPath implements IMatchPath  {

    private PlayStatusEnum status;

    public TeamPath(PlayStatusEnum status) {
        this.status = status;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof Match) {
            Match match = (Match)model;
            if (match.getIsEnded()) {
                Team team = null;
                switch(status) {
                    case WINNER : {
                        team = match.getWinner();
                        break;
                    } 
                    case LOSER : {
                        team = match.getLoser();
                        break;
                    } 
                    default : {
                        break;
                    }
                }
                return team;

            }
            return null;
        }
        else {
            throw new MatchPathException("expecting a round, found "+model.getClass().getName());
        }
    }
}
