package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.IPingModel;

public interface IMatchPath {

    public IPingModel accept(IPingModel model) throws MatchPathException; 
    
}
