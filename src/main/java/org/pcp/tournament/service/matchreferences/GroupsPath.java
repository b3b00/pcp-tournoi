package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.IPingModel;
import org.pcp.tournament.model.Tournament;

public class GroupsPath implements IMatchPath {

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof Tournament) {
            Tournament t = (Tournament)model;            
            return t.getRun().getGroupPhase();
        }
        else {
            throw new MatchPathException("expecting a tournament , found "+model.getClass().getName());
        }
    }
}

