package org.pcp.tournament.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TeamGroup {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToMany
    private List<Team> teams;


    public TeamGroup() {
teams = new ArrayList<Team>();
    }

    public TeamGroup(String name) {
        teams = new ArrayList<Team>();
    }

    public void AddTeam(Team team) {
        teams.add(team);
    }

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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the teams
     */
    public List<Team> getTeams() {
        return teams;
    }

    /**
     * @param teams the teams to set
     */
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        String content = "Group [" + getName() + "]";
        for (Team team : getTeams()) {
            content += team.toString() + "\n";
        }
        return content;
    }

}