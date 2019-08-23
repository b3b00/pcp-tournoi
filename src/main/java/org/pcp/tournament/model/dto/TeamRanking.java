package org.pcp.tournament.model.dto;

import org.pcp.tournament.model.Team;

public class TeamRanking implements Comparable<TeamRanking> {

    private Team team;

    private int points;

    private int difference;

    public TeamRanking(Team team, int points, int difference) {
        this.team = team;
        this.points = points;
        this.difference = difference;
    }

    public Team getTeam() {
        return team;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
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
                return -Integer.compare(getDifference(), other.getDifference());
            }
        }
        return -1;
    }

    public boolean equals(Object o) {
        return true;
    }

    public int hashCode() {
        return team.hashCode() + points + difference;
    }

}