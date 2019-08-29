package org.pcp.tournament.service.matchreferences;

import java.util.List;

import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.IPingModel;
import org.pcp.tournament.model.dto.TeamRanking;

public class RankedInGroupPhasePath implements IMatchPath {

    private int ranking;

    public RankedInGroupPhasePath(int ranking) {
        this.ranking = ranking;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof GroupPhase) {
            GroupPhase phase = (GroupPhase)model;  
            List<TeamRanking> rankings = phase.getFullRanking();
            if (ranking >= 0 && ranking < rankings.size()) {
                return rankings.get(ranking).getTeam();
            }
            else {
                throw new MatchPathException("no team ranked #"+ranking);    
            }
        }
        else {
            throw new MatchPathException("expecting a group phase , found "+model.getClass().getName());
        }
    }

}

