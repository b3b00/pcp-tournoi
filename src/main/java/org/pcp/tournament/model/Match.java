package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Match implements IPingModel {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Team leftTeam;

    @Transient
    @JsonInclude
    private int leftWonSet = 0;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Team rightTeam;

    @Transient
    @JsonInclude
    private int rightWonSet = 0;

    @Transient
    @JsonInclude
    private int rightPoints = 0;

    @Transient
    @JsonInclude
    private int leftPoints = 0;

    @OneToMany(mappedBy = "match",cascade=CascadeType.REMOVE)
    private List<MatchSet> score;

    @Transient
    @JsonInclude
    private Team winner;

    @Transient
    @JsonInclude
    private boolean isEnded;

    @JsonIgnore
    private String leftTeamReference;

    @JsonIgnore
    private String rightTeamReference;

    @Transient
    private String leftTeamReferenceLabel;

    @Transient
    private String rightTeamReferenceLabel;

    private boolean isFinale;

    private boolean isSemiFinale;

    public Match() {
        score = new ArrayList<MatchSet>();
    }

    public boolean isSemiFinale() {
        return isSemiFinale;
    }

    public void setSemiFinale(boolean isSemiFinale) {
        this.isSemiFinale = isSemiFinale;
    }

    public boolean isFinale() {
        return isFinale;
    }

    public void setFinale(boolean isFinale) {
        this.isFinale = isFinale;
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

    public Team getLoser() {
        if (winner != null) {
            if (winner.getId() == leftTeam.getId()) {
                return rightTeam;
            }
            return leftTeam;
        }
        return null;
    }

    /**
     * @param winner the winner to set
     */
    public void setWinner(Team winner) {
        this.winner = winner;
    }

    /**
     * @return the leftWonSet
     */
    public int getLeftWonSet() {
        return leftWonSet;
    }

    /**
     * @param leftWonSet the leftWonSet to set
     */
    public void setLeftWonSet(int leftWonSet) {
        this.leftWonSet = leftWonSet;
    }

    public int getRightWonSet() {
        return rightWonSet;
    }

    public int getLeftPoints() {
        return leftPoints;
    }

    public void setLeftPoints(int leftPoints) {
        this.leftPoints = leftPoints;
    }

    public int getRightPoints() {
        return rightPoints;
    }

    public void setRightPoints(int rightPoints) {
        this.rightPoints = rightPoints;
    }

    public void setRightWonSet(int rightWonSet) {
        this.rightWonSet = rightWonSet;
    }

    public String getLeftTeamReference() {
        return leftTeamReference;
    }

    private String getTeamReferenceLabel(String reference) {
        String label = "";
        if (reference != null) {
            String groupPrefix = "groups/group/";
            String roundPrefix = "boards/board/";
            if (reference.startsWith(groupPrefix)) {
                String t = reference.replace(groupPrefix, "");
                String[] items = t.split("\\/");
                if (items[1].equals("0")) {
                    label = "1er";
                } else if (items[1].equals("1")) {
                    label = "2ème";
                }
                label += " du groupe " + items[0];
            }            
            else if (reference.startsWith(roundPrefix)) {
                label = "&Phi; - &Phi;";
            }
        }
        return label;
    }

    public String getLeftTeamReferenceLabel() {
        leftTeamReferenceLabel = getTeamReferenceLabel(leftTeamReference);
        return leftTeamReferenceLabel;
    }

    public String getRightTeamReferenceLabel() {
        rightTeamReferenceLabel = getTeamReferenceLabel(rightTeamReference);
        return rightTeamReferenceLabel;
    }

    public void setLeftTeamReference(String leftTeamReference) {
        this.leftTeamReference = leftTeamReference;
    }

    public String getRightTeamReference() {
        return rightTeamReference;
    }

    public void setRightTeamReference(String rightTeamReference) {
        this.rightTeamReference = rightTeamReference;
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
        leftWonSet = 0;
        rightWonSet = 0;
        for (MatchSet matchSet : getScore()) {
            Team setWinner = winner(matchSet, options);
            if (setWinner != null) {
                if (setWinner.getId() == getLeft().getId()) {
                    leftWonSet++;
                } else {
                    rightWonSet++;
                }
            }
            setLeftPoints(leftPointDifference());
            setRightPoints(rightPointDifference());
        }
        setIsEnded(false);
        if (leftWonSet >= options.getWinningSets()) {
            setWinner(getLeft());
            setIsEnded(true);
        }
        if (rightWonSet >= options.getWinningSets()) {
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
        if (right >= len && (right - left) >= 2) {
            return getRight();
        }
        return null;
    }

    public String toString() {
        return getLeft().getName() + " - " + getRight().getName();
    }

}