package org.pcp.tournament.service.matchreferences;

import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.IPingModel;

public class GroupPath implements IMatchPath {

    private String groupName;

    public GroupPath(String groupName) {
        this.groupName = groupName;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof GroupPhase) {
            GroupPhase phase = (GroupPhase)model;            
            GroupPlay play =  phase.getGroups().stream()
                .filter(p -> p.getGroup().getName().equals(groupName))
                .findAny()
                .orElse(null);
            if (play != null) {
                return play;                
            }
            else {
                throw new MatchPathException("group "+groupName+" not found");
            }
        }
        else {
            throw new MatchPathException("expecting a group phase , found "+model.getClass().getName());
        }
    }
} 

