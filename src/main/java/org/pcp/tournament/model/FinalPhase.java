package org.pcp.tournament.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;


@Entity
public class FinalPhase implements IPingModel{


    @Id
    @GeneratedValue
    private int id;

    private String name;

    @OneToMany(mappedBy = "phase",cascade = CascadeType.REMOVE)
    private List<Round> rounds;


    public FinalPhase() {
        this.rounds = new ArrayList<Round>();
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
     * @return the rounds
     */
    public List<Round> getRounds() {
        return rounds;
    }

    /**
     * @param rounds the rounds to set
     */
    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }


    public void addRound(Round round) {
        this.rounds.add(round);
    }

	public void computeScores(Options options) {
        for (Round round : rounds) {
            round.computeScores(options);
        }
    }
    
    public boolean getIsDone() {
        return getRounds().stream().map(r -> r.isDone()).reduce(true, (x,y) -> x && y);
    }
}