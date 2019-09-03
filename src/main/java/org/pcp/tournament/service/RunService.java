package org.pcp.tournament.service;

import java.util.ArrayList;
import java.util.List;

import org.pcp.tournament.model.*;
import org.pcp.tournament.dao.*;

import org.pcp.tournament.service.matchreferences.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.javatuples.Pair;

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
    FinalPhaseDao finalPhaseDao;

    @Autowired
    RoundDao roundDao;

    @Autowired
    RunDao runDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    TournamentBoardDao tournamentBoardDao;

    @Autowired
    MatchService matchService;

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
                for (GroupPlay play : plays) {
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

    public Tournament buildGroupPhase(Tournament tournament) {
        if (tournament != null) {
            try {
                Options options = tournament.getOptions();
                GroupPhase phase = new GroupPhase();
                phase = groupPhaseDao.save(phase); // to allow group -> phase linking
                for (Group group : tournament.getGroups()) {
                    GroupPlay play = new GroupPlay();
                    play.setGroup(group);

                    List<Team> teams = group.getTeams();
                    List<Match> matches = new ArrayList<Match>();

                    int len = teams.size();
                    for (int i = len - 1; i >= 0; i--) {
                        for (int j = i - 1; j >= 0; j--) {
                            Team t1 = teams.get(i);
                            Team t2 = teams.get(j);
                            Match match = new Match();
                            match.setLeft(t1);
                            match.setRight(t2);
                            Match newMatch = matchService.createMatch(match, options);
                            matches.add(newMatch);
                        }
                    }
                    play.setMatches(matches);
                    play.setPhase(phase);
                    groupPlayDao.save(play);
                    phase.addGroupPlay(play);
                }
                phase = groupPhaseDao.save(phase);
                Run run = tournament.getRun();
                if (run == null) {
                    run = new Run(tournament, phase);
                }
                run.setGroupPhase(phase);
                runDao.save(run);
                phase.setRun(run);
                groupPhaseDao.save(phase);
                tournament.setRun(run);
                tournament = tournamentDao.save(tournament);
                return tournament;
            } catch (Exception e) {
                throw e;
            }            
        }
        return null;
    }

    public void buildBoard(Tournament tournament, int startingRound) {

        if (tournament.getGroups().size() == startingRound) {
            buildBoardNominal(tournament, startingRound);
        } else {
            // TODO later if really needed
        }

    }

    public void buildBoardNominal(Tournament tournament, int startingRound) {
        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = new TournamentBoard();
        board = tournamentBoardDao.save(board);

        tournament.getRun().setBoard(board);
        tournament = tournamentDao.save(tournament);

        finalPhase.setName("I");
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);
        Round previous = buildNominalFirstRound(tournament, finalPhase, startingRound);        
        for (int i = startingRound/2; i > 1; i = i/2 ) 
        {
            previous = buildRoundNominal(tournament, finalPhase, previous, i);
        }
        buildFinalRound(tournament,finalPhase,previous);
    }

    private Round buildFinalRound(Tournament tournament, FinalPhase finalPhase, Round previous) {
        Round round = new Round();
        round.setPhase(finalPhase);        
        
        Match finalMatch = new Match(); 
        finalPhase.addRound(round);
        finalPhaseDao.save(finalPhase);
        String leftRef = builMatchPath(previous, 0, PlayStatusEnum.WINNER);
        String rightRef = builMatchPath(previous, 1, PlayStatusEnum.WINNER);
        finalMatch.setLeftTeamReference(leftRef);
        finalMatch.setRightTeamReference(rightRef);
        finalMatch = matchDao.save(finalMatch);

        Match smallFinalMatch = new Match(); 
        leftRef = builMatchPath(previous, 0, PlayStatusEnum.LOSER);
        rightRef = builMatchPath(previous, 1, PlayStatusEnum.LOSER);
        smallFinalMatch.setLeftTeamReference(leftRef);
        smallFinalMatch.setRightTeamReference(rightRef);
        smallFinalMatch = matchDao.save(smallFinalMatch);

        round.addMatch(smallFinalMatch);

        return round;
    }

    private Round buildRoundNominal(Tournament tournament,FinalPhase finalPhase, Round previous, int number) {
        Round round = new Round();
        round.setPhase(finalPhase);
        finalPhase.addRound(round);
        finalPhaseDao.save(finalPhase);
        for(int i = 0; i < number; i++) {
            Match match = new Match();
            String leftRef = builMatchPath(previous, i*2, PlayStatusEnum.WINNER);
            String rightRef = builMatchPath(previous, i*2+1, PlayStatusEnum.WINNER);
            match.setLeftTeamReference(leftRef);
            match.setRightTeamReference(rightRef);
            match = matchDao.save(match);
            round.addMatch(match);
            
        }
        return round;
    }


    private Round buildNominalFirstRound(Tournament tournament, FinalPhase finalPhase, int startingRound) {
        Round start = new Round();
        start.setPhase(finalPhase);

        for (int i = 0; i < startingRound / 2; i++) {

            Group leftGroup = tournament.getGroups().get(i);
            Group rightGroup = tournament.getGroups().get(startingRound - i - 1);

            Match match1 = new Match();
            match1.setLeftTeamReference(buildMatchPath(leftGroup, 0));
            match1.setRightTeamReference(buildMatchPath(rightGroup, 1));
            match1 = matchDao.save(match1);

            Match match2 = new Match();
            match2.setLeftTeamReference(buildMatchPath(rightGroup, 0));
            match2.setRightTeamReference(buildMatchPath(leftGroup, 1));
            match2 = matchDao.save(match2);

            start.addMatch(match1);
            start.addMatch(match2);

            start = roundDao.save(start);

        }
        finalPhase.addRound(start);
        finalPhase = finalPhaseDao.save(finalPhase);

        return start;
    }

    /*
     * 
     * si x = niveau de départ (ex 8 pour les 8emes) pour n poules.
     * 
     * cas nominal : n = x => on sélectionne les 2 1eres de chaque poule
     * 
     * iteration 1 : n < x => les 2 1eres de chaque poules puis - si 1 ou + poule(s)
     * avec plus de joueurs que la moyenne on prend 3 , 4 .... jusqu'à atteindre 2x
     * sélectionnées.
     * 
     * 
     * 
     * 
     * 
     * 
     */

    // region [match referencing]

    public static String groupsPath = "groups";
    public static String groupPath = "group";
    public static String boardsPath = "boards";
    public static String boardPath = "board";
    public static String roundPath = "round";
    public static String matchPath = "match";

    public String buildMatchPath(Group group, int rank) {
        PathBuilder pathBuilder = new PathBuilder();
        pathBuilder.append(groupsPath).append(groupPath).append(group.getName()).append(rank);
        return pathBuilder.toString();
    }

    public String buildMatchPath(int rank) {
        PathBuilder pathBuilder = new PathBuilder();
        pathBuilder.append(groupsPath).append(rank);
        return pathBuilder.toString();
    }

    public String builMatchPath(Round round, int matchNumber, PlayStatusEnum status) {
        PathBuilder pathBuilder = new PathBuilder();
        FinalPhase phase = round.getPhase();
        pathBuilder.append(boardsPath).append(boardPath).append(phase.getName()).append(roundPath)
                .append(round.getMatches().size()).append(matchPath).append(matchNumber).append(status.toString());
        return pathBuilder.toString();
    }

    public String[] checkPath(String path, Tournament tournament) throws MatchPathException {
        String[] elements = path.split("\\/");

        if (elements != null && elements.length > 1) {
            String start = elements[0];
            if (start.equals(groupsPath)) {
                parseGroupPath(elements, tournament);
            } else if (start.equals(boardsPath)) {
                checkBoardPath(elements, tournament);
            }
        }

        return elements;
    }

    private List<IMatchPath> parseGroupPath(String[] elements, Tournament tournament) throws MatchPathException {
        List<IMatchPath> path = new ArrayList<IMatchPath>();
        path.add(new GroupsPath());
        path.add(new GroupsPath());
        if (elements[1].equals(groupPath)) {
            String name = elements[2];
            int i = 0;
            boolean found = false;
            List<Group> groups = tournament.getGroups();
            while (!found && i < groups.size()) {
                Group group = groups.get(i);
                found = group.getName().equals(name);
                i++;
            }
            if (!found) {
                throw new MatchPathException("group " + name + " does not exists.");
            }
            path.add(new GroupPath(name));

            Pair<Boolean, Integer> parsedInt = tryParseInt(elements[3]);
            if (parsedInt.getValue0()) {
                path.add(new RankedInGroupPath(parsedInt.getValue1()));
            } else {
                throw new MatchPathException(elements[1] + " is not a ranking");
            }

        } else {
            Pair<Boolean, Integer> parsedInt = tryParseInt(elements[1]);
            if (parsedInt.getValue0()) {
                path.add(new RankedInGroupPhasePath(parsedInt.getValue1()));
            } else {
                throw new MatchPathException(elements[1] + " is not a ranking");
            }
        }
        return path;
    }

    private void checkBoardPath(String[] elements, Tournament tournament) {

    }

    private Pair<Boolean, Integer> tryParseInt(String element) {
        int i = 0;
        boolean ok = false;
        try {
            i = Integer.parseInt(element);
            ok = true;
        } catch (NumberFormatException e) {
            i = 0;
            ok = false;
        }
        return new Pair<Boolean, Integer>(ok, i);
    }

    // endregion

}