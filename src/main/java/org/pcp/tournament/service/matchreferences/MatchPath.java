package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.*;


public class MatchPath implements IMatchPath  {

    private int index;

    public MatchPath(int index) {
        this.index = index;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof Round) {
            Round round = (Round)model;
            
            if (index >= 0 && index < round.getName()) {
                return round.getMatches().get(index);
            }
            else {
                throw new MatchPathException("round "+round.getName()+" does not have match #"+index);
            }
        }
        else {
            throw new MatchPathException("expecting a round, found "+model.getClass().getName());
        }
    }
}
