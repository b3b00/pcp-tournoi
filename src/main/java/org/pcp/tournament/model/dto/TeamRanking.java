package org.pcp.tournament.model.dto;

import org.pcp.tournament.model.Team;

public class TeamRanking implements Comparable<TeamRanking> {

    private Team team;

    private int points;

    private int setDifference;

    private int pointsDifference;

    public TeamRanking(Team team, int points, int setDifference, int pointsDifference) {
        this.team = team;
        this.points = points;
        this.setDifference = setDifference;
        this.setPointsDifference(pointsDifference);
    }

    public Team getTeam() {
        return team;
    }

    public int getSetDifference() {
        return setDifference;
    }

    public void setSetDifference(int dsetDifference) {
        this.setDifference = dsetDifference;
    }

    public int getPointsDifference() {
        return pointsDifference;
    }

    public void setPointsDifference(int pointsDifference) {
        this.pointsDifference = pointsDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int compareTo(TeamRanking other) {
        if (other != null) {
            if (getPoints() > other.getPoints()) {
                return -1;
            } else {
                int comp = - Integer.compare(getSetDifference(), other.getSetDifference());
                if (comp == 0) {
                    comp = - Integer.compare(getPointsDifference(), other.getPointsDifference());
                }
                return comp;
            }
        }
        return -1;
    }

    public boolean equals(Object o) {
        return true;
    }

    public int hashCode() {
        return team.hashCode() + points + setDifference;
    }

    public String toString() {
        return team.getName()+" - "+getPoints()+" - "+getSetDifference()+ " - " + getPointsDifference();
    }

}