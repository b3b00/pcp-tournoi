package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.pcp.tournament.model.dto.TeamRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "group_play")
public class GroupPlay implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @JsonInclude
    @Transient
    private List<TeamRanking> rankings;

    @OneToOne
    private org.pcp.tournament.model.Group group;

    @OneToMany
    private List<Match> matches;

    @ManyToOne
    @JsonIgnore
    private GroupPhase phase;

    @Transient  
    private boolean isDone;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * @return the matches
     */
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * @param matches the matches to set
     */
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    /**
     * @return the rankings
     */
    public List<TeamRanking> getRankings() {
        return rankings;
    }

    /**
     * @param rankings the rankings to set
     */
    public void setRankings(List<TeamRanking> rankings) {
        this.rankings = rankings;
    }

    public GroupPhase getPhase() {
        return phase;
    }

    public void setPhase(GroupPhase phase) {
        this.phase = phase;
    }

    /**
     * @return the isDone
     */
    public boolean getIsDone() {
        return (matches.stream().allMatch(m -> m.getIsEnded()));
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    private TeamRanking updateRanking(TeamRanking ranking, Team team, Match match, Options options) {
        match.compute(options);
        boolean isLeft = match.getLeft().getId() == team.getId();
        boolean isRight = match.getRight().getId() == team.getId();
        if (ranking == null) {
            ranking = new TeamRanking(team, 0, 0,0);
        }
        Team winner = match.getWinner();
        if (winner != null) {
            int point = match.getWinner().getId() == team.getId() ? 1 : 0;
            int diff = 0;

            if (isLeft) {
                diff = match.getLeftWonSet() - match.getRightWonSet();
            } else if (isRight) {
                diff = match.getRightWonSet() - match.getLeftWonSet();
            }

            ranking.setPoints(ranking.getPoints() + point);
            ranking.setSetDifference(ranking.getSetDifference() + diff);
            ranking.setPointsDifference(ranking.getPointsDifference());
        }
        boolean isAllDone = (matches.stream().allMatch(m -> m.getIsEnded()));
        setIsDone(isAllDone);
        return ranking;

    }

    public void computeRanking() {
        rankings = new ArrayList<TeamRanking>();

        Map<Team, TeamRanking> rankingByTeam = new HashMap<Team, TeamRanking>();
        Options options = getGroup().getTournament().getOptions();
        for (Match match : getMatches()) {
            TeamRanking tr1 = rankingByTeam.get(match.getLeft());
            tr1 = updateRanking(tr1, match.getLeft(), match, options);
            rankingByTeam.put(match.getLeft(), tr1);
            TeamRanking tr2 = rankingByTeam.get(match.getRight());
            tr2 = updateRanking(tr2, match.getRight(), match, options);
            rankingByTeam.put(match.getRight(), tr2);
        }
       
        rankings.addAll(rankingByTeam.values());
        Collections.sort(rankings);
    }

    public boolean hasStarted() {
        for (Match match : matches) {
            if (match.getScore().size() > 0) {
                for (MatchSet set : match.getScore()) {
                    if (set.getLeft()+set.getRight() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}