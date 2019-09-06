package org.pcp.tournament.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="group_table")
public class Group implements IPingModel {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToMany(mappedBy = "group",cascade=CascadeType.REMOVE)
    private List<Team> teams;

    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Tournament tournament;

    public Group() {
        teams = new ArrayList<Team>();
    }

    public Group(String name) {
        setName(name);
        teams = new ArrayList<Team>();
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

    public int size() {
        return teams.size();
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addTeams(List<Team> teams) {
        teams.addAll(teams);
    }

    /**
     * @return the tournament
     */
    public Tournament getTournament() {
        return tournament;
    }

    /**
     * @param tournament the tournament to set
     */
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
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