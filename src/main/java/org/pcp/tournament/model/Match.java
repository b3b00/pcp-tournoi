package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Match {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Team leftTeam;

    @OneToOne
    private Team rightTeam;

    @OneToMany
    private List<MatchSet> score;

    @Transient
    @JsonInclude
    private Team winner;

    @Transient
    @JsonInclude
    private boolean isEnded;


    public Match() {
        score = new ArrayList<MatchSet>();
    }

    public int getId() {
        return id;
    }

    public Team getLeft() {
        return leftTeam;
    }

    public void setLeft(Team left) {
        this.leftTeam = left;
    }

    public Team getRight() {
        return rightTeam;
    }

    public void setRight(Team right) {
        this.rightTeam = right;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MatchSet> getScore() {
        return this.score;
    }

    public void setScore(List<MatchSet> sets) {
        this.score = sets;
    }

    public void addSet(MatchSet set) {
        this.score.add(set);
    }

    public MatchSet getSet(int i) {
        return this.score.get(i);
    }

    public boolean getIsEnded() {
        return isEnded;
    }

    public void setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }

    /**
     * @return the winner
     */
    public Team getWinner() {
        return winner;
    }

    /**
     * @param winner the winner to set
     */
    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public int pointDifference(Team team) {
        if (team.getId() == getLeft().getId())  {
            return leftPointDifference();
        }
        else {
            return rightPointDifference();
        }
    }

    private int leftPointDifference() {
        int diff = 0;
        for (MatchSet matchSet : getScore()) {
            diff += matchSet.getLeft() - matchSet.getRight();
        }
        return diff;
    }

    private int rightPointDifference() {
        int diff = 0;
        for (MatchSet matchSet : getScore()) {
            diff += matchSet.getLeft() - matchSet.getRight();
        }
        return diff;
    }

    public void compute(Options options) {
        int leftWon = 0;
        int rightWon = 0;
        for (MatchSet matchSet : getScore()) {
            Team setWinner = winner(matchSet, options);
            if (setWinner != null) {
                if (setWinner.getId() == getLeft().getId()) {
                    leftWon++;
                }
                else {
                    rightWon++;
                }
            }
        }
        setIsEnded(false);
        if (leftWon >= options.getWinningSets()) {
            setWinner(getLeft());
            setIsEnded(true);
        }
        if (rightWon >= options.getWinningSets()) {
            setWinner(getRight());
            setIsEnded(true);
        }
        
    }

    private Team winner(MatchSet set, Options options) {
        int left = set.getLeft();
        int right = set.getRight();
        int len = options.getSetLength();
        if (left >= len && (left - right) >= 2) {
            return getLeft();
        }
        if (right >= len && (right-left) >= 2) {
            return getRight();
        }
        return null;
    }

    public String  toString() {
        return getLeft().getName()+" - "+getRight().getName();
    }

}