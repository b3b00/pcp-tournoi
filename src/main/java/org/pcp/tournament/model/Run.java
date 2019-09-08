package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Run implements IPingModel  {


    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @OneToOne
    private Tournament tournament;

    @OneToOne(mappedBy = "run",cascade=CascadeType.REMOVE)
    private GroupPhase groupPhase;

    @OneToOne(cascade=CascadeType.REMOVE)
    private TournamentBoard board;

    public Run() {
        this.tournament = null;
        this.groupPhase =null;
    }

    public Run(Tournament tournament, GroupPhase groupPhase) {
        this.tournament = tournament;
        this.groupPhase = groupPhase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public GroupPhase getGroupPhase() {
        return groupPhase;
    }

    public void setGroupPhase(GroupPhase groupPhase) {
        this.groupPhase = groupPhase;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    /**
     * @return the board
     */
    public TournamentBoard getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(TournamentBoard board) {
        this.board = board;
    }

    


}