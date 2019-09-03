package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.IPingModel;

public class RankedInGroupPath implements IMatchPath {

    private int ranking;

    public RankedInGroupPath(int ranking) {
        this.ranking = ranking;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof GroupPlay) {
            GroupPlay play = (GroupPlay)model;  
            if (play.hasStarted() && ranking >= 0 && ranking < play.getRankings().size()) {
                return play.getRankings().get(ranking).getTeam();
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

}

