package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.FinalPhase;
import org.pcp.tournament.model.IPingModel;
import org.pcp.tournament.model.Round;

public class RoundPath implements IMatchPath  {

    private int index;

    public RoundPath(int index) {
        this.index = index;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof FinalPhase) {
            FinalPhase board = (FinalPhase)model;
            Round round = board.getRounds().stream()
            .filter(r -> r.getName() == index)
            .findAny()
            .orElse(null);
            if (round != null) {
                return round;
            }
            else {
                throw new MatchPathException("phase "+board.getName()+" does not have round #"+index);
            }
        }
        else {
            throw new MatchPathException("expecting a final phase, found "+model.getClass().getName());
        }
    }
}

