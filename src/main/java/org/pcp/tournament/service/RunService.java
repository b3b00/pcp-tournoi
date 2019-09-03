package org.pcp.tournament.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pcp.tournament.model.*;
import org.pcp.tournament.model.dto.*;
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
            int nbGroups = tournament.getGroups().size();
            int nbSelectedByGroup = startingRound / nbGroups;
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

            int count = selection.stream().map(s -> s.size()).reduce(0, (Integer a, Integer b) -> a + b);
            if (count < startingRound) {
                List<Integer> selectedIds = selected.stream().map(tr -> tr.getTeam().getId())
                        .collect(Collectors.toList());
                int missingCount = startingRound - count;
                List<TeamRanking> full = tournament.getRun().getGroupPhase().getFullRanking();
                for (int i = 0; i < missingCount; i++) {
                    TeamRanking team = null;
                    int j = 0;
                    while (team == null && j < full.size()) {
                        TeamRanking rank = full.get(j);
                        if (!selectedIds.contains(rank.getTeam().getId())) {
                            team = rank;
                            selectedIds.add(team.getTeam().getId());
                            selected.add(team);
                        }
                        j++;
                    }
                }
            }
        }

    }

    public void buildBoardNominal(Tournament tournament, int startingRound) {
        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = new TournamentBoard();
        board = tournamentBoardDao.save(board);
        finalPhase.setName("I");
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);
        Round start = new Round();
        start.setPhase(finalPhase);

        for (int i = 0; i < startingRound / 2; i++) {

            Group leftGroup = tournament.getGroups().get(i);
            Group rightGroup = tournament.getGroups().get(startingRound - i - 1);

            String leftGroupPath = String.format("/groups/group/%s/", leftGroup.getName());
            String rightGroupPath = String.format("/groups/group/%s/", rightGroup.getName());

            Match match1 = new Match();
            match1.setLeftTeamReference(leftGroupPath + "0");
            match1.setRightTeamReference(rightGroupPath + "1");
            match1 = matchDao.save(match1);

            Match match2 = new Match();
            match2.setLeftTeamReference(leftGroupPath + "1");
            match2.setRightTeamReference(rightGroupPath + "0");
            match2 = matchDao.save(match2);

            start.addMatch(match1);
            start.addMatch(match2);

            start = roundDao.save(start);
            finalPhase.addRound(start);
            finalPhase = finalPhaseDao.save(finalPhase);

        }

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

    public String buildMatchPath(GroupPlay group, int rank) {
        PathBuilder pathBuilder = new PathBuilder();
        pathBuilder.append(groupsPath).append(groupPath).append(group.getGroup().getName()).append(rank);
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