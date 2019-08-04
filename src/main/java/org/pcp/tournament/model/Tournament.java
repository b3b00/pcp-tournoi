package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Options options;

    private String name;

    @OneToMany
    private List<Player> players;

    @OneToMany
    private List<Team> teams;

    @OneToMany
    private List<TeamGroup> groups;


    public Tournament() {
        players = new ArrayList<Player>();
        teams = new ArrayList<Team>();
    }

    public Tournament(String name) {
        setName(name);
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
     * @return the options
     */
    public Options getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(Options options) {
        this.options = options;
    }

    /**
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
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

    /**
     * @return the groups
     */
    public List<TeamGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<TeamGroup> groups) {
        this.groups = groups;
    }
    

}
