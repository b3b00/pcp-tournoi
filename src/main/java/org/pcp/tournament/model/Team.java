package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Player player1;

    @OneToOne
    private Player player2;

    @JsonInclude()
    @Transient
    private String name;

    @JsonIgnore
    @ManyToOne
    private Group group;

    @JsonIgnore
    @ManyToOne
    private Tournament tournament;

    public Team() {

    }

    public Team(Player p1, Player p2) {
        setPlayer1(p1);
        setPlayer2(p2);
    }

    public Boolean isSingle() {
        return player2 == null || player1 == null;
    }

    public Boolean isDouble() {
        return !isSingle();
    }

    public Boolean isEmpty() {
        return player1 == null && player2 == null;
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
     * @return the player1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @param player1 the player1 to set
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * @return the player2
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * @param player2 the player2 to set
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * @return the name
     */
    public String getName() {
        if (isDouble()) {
            return getPlayer1().toString() + " - " + getPlayer2().toString();
        } else {
            Player player = player1 != null ? player1 : player2;
            return player.toString();
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {

    }

    /**
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Boolean containsPlayer(Player player) {
        if (player != null) {
            return this.player1.getId() == player.getId() || this.player2.getId() == player.getId();
        }
        return false;
    }

    public void RemovePlayer(Player player) {
        if (player != null) {
            if (player.getId() == player1.getId()) {
                player1 = null;
            }
            if (player.getId() == player2.getId()) {
                player2 = null;
            }
        }
    }

    @Override
    public String toString() {
        if (isDouble()) {
            return "DOUBLE " + getName();
        } else {
            return "SINGLE " + getName();
        }
    }

    public void addPlayer(Player player) {
        if (getPlayer1() == null) {
            setPlayer1(player);
        } else if (getPlayer2() == null) {
            setPlayer2(player);
        }
    }

}