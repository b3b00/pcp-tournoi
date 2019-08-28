package org.pcp.tournament.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pcp.tournament.model.*;
import org.pcp.tournament.model.dto.*;
import org.pcp.tournament.dao.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.javatuples.Pair;



interface IMatchPath {

    IPingModel accept(IPingModel model) throws MatchPathException;
    
}

class GroupsPath implements IMatchPath {

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

class GroupPath implements IMatchPath {

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

class RankedInGroupPath implements IMatchPath {

    private int ranking;

    public RankedInGroupPath(int ranking) {
        this.ranking = ranking;
    }

    public IPingModel accept(IPingModel model) throws MatchPathException {
        if (model instanceof GroupPlay) {
            GroupPlay play = (GroupPlay)model;  
            if (ranking >= 0 && ranking < play.getRankings().size()) {
                return play.getRankings().get(ranking).getTeam();
            }
            else {
                throw new MatchPathException("no team ranked #"+ranking);    
            }
        }
        else {
            throw new MatchPathException("expecting a group play , found "+model.getClass().getName());
        }
    }

}

class RankedInGroupPhasePath implements IMatchPath {

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


class BoardsPath implements IMatchPath  {


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


class BoardPath implements IMatchPath  {

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

class RoundPath implements IMatchPath  {

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


class MatchPath implements IMatchPath  {

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

class PathBuilder {
    StringBuilder builder;

    public PathBuilder() {
        builder = new StringBuilder();        
    } 

    public PathBuilder append(String pathElement) {
        builder.append("/")
        .append(pathElement);
        return this;
    }

    public PathBuilder append(int pathElement) {
        return append(Integer.toString(pathElement));
    }
}
@Component
public class RunService {

    @Autowired
    MatchSetDao matchSetDao;

    @Autowired
    MatchDao matchDao;

    @Autowired
    GroupPlayDao groupPlayDao;

    @Autowired
    GroupPhaseDao groupPhaseDao;

    @Autowired
    RunDao runDao;

    @Autowired
    TournamentDao tournamentDao;

    public void deleteRunForTournament(int TournamentId) {
        Tournament tournament = tournamentDao.findById(TournamentId);
        if (tournament != null) {
            deleteRunForTournament(tournament);
        }
    }

    public void deleteRunForTournament(Tournament tournament) {
        Run run = tournament.getRun();
        List<GroupPlay> plays = new ArrayList<GroupPlay>();
        List<Match> matches = new ArrayList<Match>();
        List<MatchSet> sets = new ArrayList<MatchSet>();


        if (run != null) {
            GroupPhase groupPhase = run.getGroupPhase();
            if (groupPhase != null) {
                plays = groupPhase.getGroups();
                
                if (plays != null) {
                    for (GroupPlay play : plays) {
                        if (play != null) {
                            matches.addAll(play.getMatches());
                            for (Match match : play.getMatches()) {
                                sets.addAll(match.getScore());
                            }
                        }
                    }
                }
                sets.stream().forEach(s -> matchSetDao.delete(s));
                for(GroupPlay play : plays) {
                    play.setMatches(null);
                    groupPlayDao.save(play);
                }
                matches.stream().forEach(m -> matchDao.delete(m));
                groupPhase.setGroups(null);
                groupPhase = groupPhaseDao.save(groupPhase);
                plays.stream().forEach(p -> groupPlayDao.delete(p));
                run.setGroupPhase(null);
                run = runDao.save(run);
                groupPhaseDao.delete(groupPhase);
                tournament.setRun(null);
                tournamentDao.save(tournament);
                runDao.delete(run);
            }
            

        }
    }

    public void buildBoard(Tournament tournament, int startingRound) {
        int nbGroups = tournament.getGroups().size();
        int nbSelectedByGroup = startingRound/nbGroups;
        List<GroupPlay> groups = tournament.getRun().getGroupPhase().getGroups();
        List<List<TeamRanking>> selection = new ArrayList<List<TeamRanking>>();
        List<TeamRanking> selected = new ArrayList<TeamRanking>();
        for (GroupPlay group : groups) {
            group.computeRanking();
            List<TeamRanking> groupRanking = group.getRankings();
            List<TeamRanking> groupSelection = new ArrayList<TeamRanking>();
            if (groupRanking.size() >= nbSelectedByGroup) {
                for (int i = 0; i < nbSelectedByGroup; i++) {
                    groupSelection.add(groupRanking.get(i));
                    selected.add(groupRanking.get(i));
                }
                selection.add(groupSelection);
            }
        }

        int count = selection.stream().map(s -> s.size()).reduce(0,(Integer a,Integer b) -> a+b);
        if (count < startingRound) {
            List<Integer> selectedIds = selected.stream().map(tr -> tr.getTeam().getId()).collect(Collectors.toList());
            int missingCount = startingRound - count;
            List<TeamRanking> full = tournament.getRun().getGroupPhase().getFullRanking();
            for (int i = 0; i < missingCount; i++) {
                TeamRanking team = null;
                int j = 0;
                while(team == null && j < full.size()) {
                    TeamRanking rank = full.get(j);                    
                    if (!selectedIds.contains(rank.getTeam().getId())) {
                        team = rank;
                        selectedIds.add(team.getTeam().getId());
                        selected.add(team);                        
                    }
                    j++;
                }
            }
            System.out.println("hello");
            ;
        }


    }
    


//region [match referencing]


    public static String groupsPath = "groups";
    public static String groupPath = "group";
    public static String boardsPath = "boards";
    public static String boardPath = "board";
    public static String roundPath = "round";
    public static String matchPath = "match";

    public String buildMatchPath(GroupPlay group, int rank) {
        PathBuilder pathBuilder = new PathBuilder();
        pathBuilder.append(groupsPath)
        .append(groupPath)
        .append(group.getGroup().getName())        
        .append(rank);
        return pathBuilder.toString();
    }

    public String buildMatchPath(int rank) {
        PathBuilder pathBuilder = new PathBuilder();
        pathBuilder.append(groupsPath)        
        .append(rank);
        return pathBuilder.toString();
    }

    public String builMatchPath(Round round, int matchNumber, PlayStatusEnum status ) {
        PathBuilder pathBuilder = new PathBuilder();
        FinalPhase phase = round.getPhase();
        pathBuilder.append(boardsPath)
        .append(boardPath)
        .append(phase.getName())
        .append(roundPath)
        .append(round.getMatches().size())
        .append(matchPath)
        .append(matchNumber)
        .append(status.toString());
        return pathBuilder.toString();
    }

    public String[] checkPath(String path, Tournament tournament) throws MatchPathException {
        String[] elements = path.split("\\/");

        if (elements != null && elements.length > 1)  {
            String start = elements[0];
            if (start.equals(groupsPath)) {
                checkGroupPath(elements, tournament);
            }
            else if(start.equals(boardsPath)) {
                checkBoardPath(elements, tournament);
            }
        }

        return elements;
    }

    private Pair<Boolean,Integer> tryParseInt(String element) {
        int i = 0;
        boolean ok = false;
        try{
            i = Integer.parseInt(element);
            ok = true;
        }catch(NumberFormatException e){
            i = 0;
            ok = false;
        }
        return new Pair<Boolean,Integer>(ok, i);
    }

    

    private List<IMatchPath> checkGroupPath(String[] elements, Tournament tournament) throws MatchPathException {
        List<IMatchPath> path = new ArrayList<IMatchPath>();
        path.add(new GroupsPath());
        path.add(new GroupsPath());
        if (elements[1].equals(groupPath)) {
            String name = elements[2];
            int i = 0;
            boolean found = false;
            List<Group> groups = tournament.getGroups();
            while (! found && i <  groups.size()) {
                Group group = groups.get(i);
                found = group.getName().equals(name);
                i++;
            }
            if (!found) {
                throw new MatchPathException("group "+name+" does not exists.");
            }
            path.add(new GroupPath(name));
            
            Pair<Boolean,Integer> parsedInt = tryParseInt(elements[3]);
            if (parsedInt.getValue0()) {
                path.add(new RankedInGroupPath(parsedInt.getValue1()));
            }
            else {
                throw new MatchPathException(elements[1]+" is not a ranking");
            }

        }   
        else {
            Pair<Boolean,Integer> parsedInt = tryParseInt(elements[1]);
            if (parsedInt.getValue0()) {
                path.add(new RankedInGroupPhasePath(parsedInt.getValue1()));
            }
            else {
                throw new MatchPathException(elements[1]+" is not a ranking");
            }
        }   
        return path;     
    }


    private void checkBoardPath(String[] elements, Tournament tournament) {

    }

    

//endregion


}