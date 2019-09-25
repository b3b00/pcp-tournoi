package org.pcp.tournament.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.pcp.tournament.model.*;
import org.pcp.tournament.dao.*;

import org.pcp.tournament.service.matchreferences.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.javatuples.Pair;

@Component
public class RunService {

    // region [dependencies]

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
    TeamDao teamDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    TournamentBoardDao tournamentBoardDao;

    @Autowired
    MatchService matchService;

    // endregion

    // region [cleaning]

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

    // endregion

    // region [building with references]

    // region [groups]
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

    // endregion

    // region [boards]

    public boolean checkNominalMainBoard(Tournament tournament, int startingRound) {
        boolean matchGroupNumber = tournament.getGroups().size() >= startingRound;
        boolean matchPlayerNumber = true;
        for (Group group : tournament.getGroups()) {
            matchPlayerNumber = matchPlayerNumber && group.getTeams().size() >= 2;
        }
        return matchGroupNumber && matchPlayerNumber;
    }

    public boolean checkNominalSecondBoard(Tournament tournament, int startingRound) {
        boolean matchGroupNumber = tournament.getGroups().size() >= startingRound;
        boolean matchPlayerNumber = true;
        for (Group group : tournament.getGroups()) {
            matchPlayerNumber = matchPlayerNumber && group.getTeams().size() >= 4;
        }
        return matchGroupNumber && matchPlayerNumber;
    }

    public void buildMainBoard(Tournament tournament) {

        int startingRound = tournament.getGroups().size();

        if (tournament.getGroups().size() % 2 == 0) {

            if (checkNominalMainBoard(tournament, startingRound)) {
                buildMainBoardNominal(tournament, startingRound);
            } else {
                // TODO later if really needed
            }
            InjectTeams(tournament);
            computeTeamReferenceLabels(tournament);
        }

    }

    private static boolean isPowerOfTwo(int number) {

        return number > 0 && ((number & (number - 1)) == 0);

    }

    private int highestPowerof2(int n) {
        int res = 0;
        for (int i = n; i >= 1; i--) {
            // If i is a power of 2
            if ((i & (i - 1)) == 0) {
                res = i;
                break;
            }
        }
        return res;
    }

    public void buildSecondBoard(Tournament tournament) {
        int startingRound = tournament.getGroups().size();

        List<String> remainingTeamsPath = new ArrayList<String>();
        for (Group group : tournament.getGroups()) {
            if (group.getTeams().size() > 2) {
                for (int i = 2; i < group.getTeams().size(); i++) {
                    String path = buildMatchPath(group, i);
                    remainingTeamsPath.add(path);
                }
            }
        }

        int remainingCount = remainingTeamsPath.size();
        
        if (checkNominalSecondBoard(tournament, startingRound)) {
            buildSecondBoardNominal(tournament, startingRound);
            return;
        } 
        if (!isPowerOfTwo(remainingCount)) {
            int count = highestPowerof2(remainingCount);
            List<String> pathes = remainingTeamsPath.stream().limit(count).collect(Collectors.toList());
            buildSecondBoardNominal2(tournament, pathes);
            return;
        }
        
    }

    private void buildSecondBoardNominal2(Tournament tournament, List<String> pathes) {
        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = tournament.getRun().getBoard();
        int startingRound = pathes.size() / 2;

        if (board == null) {
            board = new TournamentBoard();
            board = tournamentBoardDao.save(board);
            tournament.getRun().setBoard(board);
            tournament = tournamentDao.save(tournament);
        }

        finalPhase.setName("consolante");
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);
        Round previous = buildNominalFirstRound2(tournament, finalPhase, pathes);
        for (int i = startingRound / 2; i > 1; i = i / 2) {
            previous = buildRoundNominal(tournament, finalPhase, previous, i);
        }
        buildFinalRound(tournament, finalPhase, previous);
    }

    private Round buildNominalFirstRound2(Tournament tournament, FinalPhase finalPhase, List<String> pathes) {
        Round start = new Round();
        start.setPhase(finalPhase);

        for (int i = 0; i < pathes.size() / 2; i++) {

            Match match = new Match();
            match = matchService.createMatch(match, tournament.getOptions());
            match.setLeftTeamReference(pathes.get(i));
            match.setRightTeamReference(pathes.get(pathes.size() - i - 1));
            match = matchDao.save(match);

            start.addMatch(match);
            start.setFinal(false);
            start = roundDao.save(start);

        }
        finalPhase.addRound(start);
        finalPhase = finalPhaseDao.save(finalPhase);

        return start;
    }

    private void buildSecondBoardNominal(Tournament tournament, int startingRound) {
        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = tournament.getRun().getBoard();

        if (board == null) {
            board = new TournamentBoard();
            board = tournamentBoardDao.save(board);
            tournament.getRun().setBoard(board);
            tournament = tournamentDao.save(tournament);
        }

        finalPhase.setName("consolante");
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);
        Round previous = buildNominalFirstRound(tournament, finalPhase, startingRound, 2, 3);
        for (int i = startingRound / 2; i > 1; i = i / 2) {
            previous = buildRoundNominal(tournament, finalPhase, previous, i);
        }
        buildFinalRound(tournament, finalPhase, previous);
    }

    public void buildMainBoardNominal(Tournament tournament, int startingRound) {
        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = tournament.getRun().getBoard();

        if (board == null) {
            board = new TournamentBoard();
            board = tournamentBoardDao.save(board);
            tournament.getRun().setBoard(board);
            tournament = tournamentDao.save(tournament);
        }

        tournament.getRun().setBoard(board);
        tournament = tournamentDao.save(tournament);

        finalPhase.setName("tableau principal");
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);
        Round previous = buildNominalFirstRound(tournament, finalPhase, startingRound, 0, 1);
        for (int i = startingRound / 2; i > 1; i = i / 2) {
            previous = buildRoundNominal(tournament, finalPhase, previous, i);
        }
        buildFinalRound(tournament, finalPhase, previous);
    }

    private Round buildFinalRound(Tournament tournament, FinalPhase finalPhase, Round previous) {
        Round round = new Round();
        round.setPhase(finalPhase);

        Match finalMatch = new Match();
        finalPhaseDao.save(finalPhase);
        String leftRef = builMatchPath(previous, 0, PlayStatusEnum.WINNER);
        String rightRef = builMatchPath(previous, 1, PlayStatusEnum.WINNER);
        finalMatch.setLeftTeamReference(leftRef);
        finalMatch.setRightTeamReference(rightRef);
        finalMatch.setFinale(true);
        finalMatch = matchService.createMatch(finalMatch, tournament.getOptions());

        Match smallFinalMatch = new Match();
        leftRef = builMatchPath(previous, 0, PlayStatusEnum.LOSER);
        rightRef = builMatchPath(previous, 1, PlayStatusEnum.LOSER);
        smallFinalMatch.setLeftTeamReference(leftRef);
        smallFinalMatch.setRightTeamReference(rightRef);
        smallFinalMatch.setSmallFinale(true);
        smallFinalMatch = matchService.createMatch(smallFinalMatch, tournament.getOptions());

        round.addMatch(finalMatch);
        round.addMatch(smallFinalMatch);
        round = roundDao.save(round);
        round.setFinal(true);
        finalPhase.addRound(round);
        finalPhase = finalPhaseDao.save(finalPhase);

        return round;
    }

    private Round buildRoundNominal(Tournament tournament, FinalPhase finalPhase, Round previous, int number) {
        Round round = new Round();
        round.setPhase(finalPhase);
        finalPhaseDao.save(finalPhase);
        for (int i = 0; i < number; i++) {
            Match match = new Match();
            String leftRef = builMatchPath(previous, i * 2, PlayStatusEnum.WINNER);
            String rightRef = builMatchPath(previous, i * 2 + 1, PlayStatusEnum.WINNER);
            match.setLeftTeamReference(leftRef);
            match.setRightTeamReference(rightRef);
            match = matchService.createMatch(match, tournament.getOptions());
            round.addMatch(match);
            round = roundDao.save(round);
        }
        round.setFinal(false);
        finalPhase.addRound(round);
        finalPhase = finalPhaseDao.save(finalPhase);
        return round;
    }

    private Round buildNominalFirstRound(Tournament tournament, FinalPhase finalPhase, int startingRound, int rankFirst,
            int rankSecond) {
        Round start = new Round();
        start.setPhase(finalPhase);

        for (int i = 0; i < startingRound; i++) {

            Group leftGroup = tournament.getGroups().get(i);
            Group rightGroup = tournament.getGroups().get(startingRound - i - 1);

            Match match1 = new Match();
            match1 = matchService.createMatch(match1, tournament.getOptions());
            match1.setLeftTeamReference(buildMatchPath(leftGroup, rankFirst));
            match1.setRightTeamReference(buildMatchPath(rightGroup, rankSecond));
            match1 = matchDao.save(match1);

            start.addMatch(match1);
            start.setFinal(false);
            start = roundDao.save(start);

        }
        finalPhase.addRound(start);
        finalPhase = finalPhaseDao.save(finalPhase);

        return start;
    }


    private Round buildFirstRoundWithTeams(Tournament tournament, FinalPhase finalPhase, List<Integer> teamsId) {
        Round start = new Round();
        start.setPhase(finalPhase);

        for (int i = 0; i < teamsId.size() / 2; i++) {
            Team team1 = teamDao.findById(teamsId.get(i*2)).get();
            Team team2 = teamDao.findById(teamsId.get(i*2+1)).get();

            Match match = new Match();
            match = matchService.createMatch(match, tournament.getOptions());
            match.setLeft(team1);
            match.setRight(team2);
            match = matchDao.save(match);

            start.addMatch(match);
            start.setFinal(false);
            start = roundDao.save(start);

        }
        finalPhase.addRound(start);
        finalPhase = finalPhaseDao.save(finalPhase);

        return start;
    }

    public Tournament buildBoardWithTeams(Tournament tournament, List<Integer> teamsId, String name) {

        FinalPhase finalPhase = new FinalPhase();
        TournamentBoard board = tournament.getRun().getBoard();

        if (board == null) {
            board = new TournamentBoard();
            board = tournamentBoardDao.save(board);
            tournament.getRun().setBoard(board);
            tournament = tournamentDao.save(tournament);
        }

        tournament.getRun().setBoard(board);
        tournament = tournamentDao.save(tournament);

        finalPhase.setName(name);
        finalPhase = finalPhaseDao.save(finalPhase);
        board.addBoard(finalPhase);
        board = tournamentBoardDao.save(board);

        Round round = buildFirstRoundWithTeams(tournament, finalPhase, teamsId);
        for (int i = teamsId.size() / 4; i > 1; i = i / 2) {
            round = buildRoundNominal(tournament, finalPhase, round, i);
        }
        buildFinalRound(tournament, finalPhase, round);

        return tournament;
    }

    // endregion

    // region [team injection]

    public void InjectTeams(Tournament tournament) {
        Run run = tournament.getRun();

        TournamentBoard board = run.getBoard();
        run.getGroupPhase().getFullRanking();
        for (FinalPhase phase : board.getBoards()) {
            InjectTeams(tournament, phase);
        }
    }

    private void InjectTeams(Tournament tournament, FinalPhase phase) {
        for (Round round : phase.getRounds()) {
            InjectTeams(tournament, round);
        }
    }

    private void InjectTeams(Tournament tournament, Round round) {
        for (Match match : round.getMatches()) {
            match.compute(tournament.getOptions());
            if (match.getLeftTeamReference() != null) {
                Team team = getTeam(tournament, match.getLeftTeamReference());
                if (team != null) {
                    match.setLeft(team);
                    match = matchDao.save(match);
                }
            }
            if (match.getRightTeamReference() != null) {
                Team team = getTeam(tournament, match.getRightTeamReference());
                if (team != null) {
                    match.setRight(team);
                    match = matchDao.save(match);
                }
            }
        }
    }

    private Team getTeam(Tournament tournament, String path) {
        try {
            List<IMatchPath> matchPath = parsePath(path, tournament);
            IPingModel previous = tournament;
            for (IMatchPath pathElement : matchPath) {
                previous = pathElement.accept(previous);
                if (previous == null) {
                    return null;
                }
            }
            if (previous instanceof Team) {
                return (Team) previous;
            } else {
                return null;
            }
        } catch (MatchPathException mpe) {
            return null;
        }
    }

    // endregion

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

    public List<IMatchPath> parsePath(String path, Tournament tournament) throws MatchPathException {
        String[] elements = path.split("\\/");

        if (elements != null && elements.length > 1) {
            String start = elements[0];
            if (start.equals(groupsPath)) {
                return parseGroupPath(elements, tournament);
            } else if (start.equals(boardsPath)) {
                return parseRoundPath(elements, tournament);
            }
        }

        throw new MatchPathException(path + " is not a valid match path");
    }

    private List<IMatchPath> parseGroupPath(String[] elements, Tournament tournament) throws MatchPathException {
        List<IMatchPath> path = new ArrayList<IMatchPath>();
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

    private List<IMatchPath> parseRoundPath(String[] elements, Tournament tournament) throws MatchPathException {
        List<IMatchPath> path = new ArrayList<IMatchPath>();
        path.add(new BoardsPath());
        String boardName = elements[2];
        path.add(new BoardPath(boardName));
        FinalPhase board = tournament.getRun().getBoard().getBoard(boardName);
        if (board == null) {
            throw new MatchPathException("board " + boardName + " does not exist.");
        }
        Pair<Boolean, Integer> parsedInt = tryParseInt(elements[4]);
        if (parsedInt.getValue0()) {
            path.add(new RoundPath(parsedInt.getValue1()));
        } else {
            throw new MatchPathException(elements[4] + " is not a round");
        }
        parsedInt = tryParseInt(elements[6]);
        if (parsedInt.getValue0()) {
            path.add(new MatchPath(parsedInt.getValue1()));
        } else {
            throw new MatchPathException(elements[6] + " is not a match");
        }

        TeamPath teamPath = new TeamPath(PlayStatusEnum.valueOf(elements[7]));
        path.add(teamPath);
        return path;
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

    // region [match labeling]

    public void computeTeamReferenceLabels(Tournament tournament) {
        if (tournament != null) {
            Run run = tournament.getRun();
            if (run != null) {
                TournamentBoard board = run.getBoard();
                if (board != null) {
                    for (FinalPhase phase : board.getBoards()) {
                        if (phase != null) {
                            for (Round round : phase.getRounds()) {
                                if (round != null) {
                                    round.getMatches().stream().forEach(m -> {
                                        m.getLeftTeamReferenceLabel();
                                        m.getRightTeamReferenceLabel();
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    // endregion

    // region [available teams]

    public List<Team> getAvailableTeams(Tournament tournament) {
        List<Team> available = new ArrayList<Team>();

        if (tournament.getRun() != null) {
            for (FinalPhase phase : tournament.getRun().getBoard().getBoards()) {
                for (Round round : phase.getRounds()) {
                    for (Match match : round.getMatches()) {
                        match.compute(tournament.getOptions());
                        if (match.getIsEnded()) {
                            if (match.isFinale() || match.isSmallFinale()) {
                                available.add(match.getLeft());
                                available.add(match.getRight());
                            }
                            else {
                                if (match.getWinner() == match.getLeft()) {
                                    available.add(match.getRight());
                                } else {
                                    available.add(match.getLeft());
                                }
                            }

                            
                        }
                    }
                }
            }

            for (GroupPlay group : tournament.getRun().getGroupPhase().getGroups()) {
                group.computeRanking();
                if (group.getIsDone()) {
                    for (Team team : group.getGroup().getTeams()) {
                        if (!isPlayingFinalPhases(tournament, team)) {
                            available.add(team);
                        }
                    }
                }
            }
        }

        return available;
    }

    public boolean isPlayingFinalPhases(Tournament tournament, Team team) {

        List<FinalPhase> finals = tournament.getRun().getBoard().getBoards();
        for (FinalPhase finalPhase : finals) {
            if (finalPhase.getRounds() != null && finalPhase.getRounds().size() > 0) {
                if (isPlayingRound(finalPhase.getRounds().get(0), team)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlayingRound(Round round, Team team) {

        for (Match match : round.getMatches()) {
            if (match.getLeft() == team || match.getRight() == team) {
                return true;
            }
        }

        return false;
    }

    // endregion
}