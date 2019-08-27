package org.pcp.tournament.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.pcp.tournament.model.Match;
import org.pcp.tournament.model.MatchSet;
import org.pcp.tournament.dao.GroupPhaseDao;
import org.pcp.tournament.dao.GroupPlayDao;
import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
import org.pcp.tournament.dao.RunDao;
import org.pcp.tournament.dao.TournamentDao;
import org.pcp.tournament.model.GroupPhase;
import org.pcp.tournament.model.GroupPlay;
import org.pcp.tournament.model.Run;
import org.pcp.tournament.model.Tournament;
import org.pcp.tournament.model.dto.TeamRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            List<TeamRanking> additional = new ArrayList<TeamRanking>();
            List<Integer> selectedIds = selected.stream().map(tr -> tr.getTeam().getId()).collect(Collectors.toList());
            int missingCount = startingRound - count;
            List<TeamRanking> full = getFullRanking(tournament);
            for (int i = 0; i < missingCount; i++) {
                TeamRanking team = null;
                int j = 0;
                while(team == null && j < full.size()) {
                    TeamRanking rank = full.get(j);
                    boolean contains = selectedIds.stream()
                    .map(sid -> rank.getTeam().getId() == sid)
                    .reduce(false , (Boolean a, Boolean b) -> a || b);
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

    private List<TeamRanking> getFullRanking(Tournament tournament) {
        List<TeamRanking> rankings = new ArrayList<TeamRanking>();

        List<GroupPlay> groups = tournament.getRun().getGroupPhase().getGroups();
        for (GroupPlay group : groups) {
            group.computeRanking();
            rankings.addAll(group.getRankings());
        }
        
        Collections.sort(rankings);

        return rankings;
    }

}