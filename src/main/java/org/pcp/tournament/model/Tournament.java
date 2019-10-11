package org.pcp.tournament.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Tournament implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Options options;

    private String name;

    @OneToMany(mappedBy = "tournament",cascade=CascadeType.REMOVE)
    private List<Player> players;

    
    @OneToMany(mappedBy = "tournament",cascade=CascadeType.REMOVE)
    private List<Team> teams;

    @OneToMany(mappedBy="tournament",cascade=CascadeType.REMOVE)
    private List<Group> groups;

    @OneToOne(mappedBy="tournament",cascade=CascadeType.REMOVE)
    private Run run;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @JsonIgnore
    private String owner;

    public Tournament() {
        players = new ArrayList<Player>();
        teams = new ArrayList<Team>();
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public void addPlayer(Player player) {
        if (this.players == null) {
            this.players = new ArrayList<Player>();
        }
        this.players.add(player);
    }

    public void addPlayers(List<Player>players) {
        if (this.players == null) {
            this.players = new ArrayList<Player>();
        }
        this.players.addAll(players);
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

    public void addTeam(Team team) {
        if (this.teams == null) {
            this.teams = new ArrayList<Team>();
        }
        this.teams.add(team);
    }

    public void addTeams(List<Team>teams) {
        if (this.teams == null) {
            this.teams = new ArrayList<Team>();
        }
        this.teams.addAll(teams);
    }

    /**
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    
    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new ArrayList<Group>();
        }
        this.groups.add(group);
    }

    public void addGroups(List<Group>groups) {
        if (this.groups == null) {
            this.groups = new ArrayList<Group>();
        }
        this.groups.addAll(groups);
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

}
