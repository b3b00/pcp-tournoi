package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.pcp.tournament.model.dto.TeamRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



@Entity
@Table(name="group_play")
public class GroupPlay {

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

    public void computeRanking() {
        rankings = new ArrayList<TeamRanking>();
        for (Team team : getGroup().getTeams()) {

            List<Match> matches = getMatches().stream().filter((Match m) -> {
                return (m.getLeft().getId() == team.getId() || 
                m.getRight().getId() == team.getId());
            }).collect(Collectors.toList());
            int points = 0;
            int difference = 0;
            for (Match match : matches) {
                match.compute(getGroup().getTournament().getOptions());
                Team winner = match.getWinner();    
                if (match.getIsEnded() && winner != null ) {
                    if (winner.getId() == team.getId()) {
                        points ++;                    
                    }
                    difference += match.pointDifference(team);
                }
            }

            TeamRanking ranking = new TeamRanking(team, points, difference);
            rankings.add(ranking);
            Collections.sort(rankings);
        }
    }

    


}