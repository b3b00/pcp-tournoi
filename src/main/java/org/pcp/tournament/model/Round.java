package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.List;

@Entity
public class Round {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    private List<Match> matches;

    @ManyToOne
    private FinalPhase phase;

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
     * @return the matches
     */
    public List<Match> getMatches() {
        return matches;
    }

    /**
     * @param matches the matches to set
     */
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    /**
     * @return the phase
     */
    public FinalPhase getPhase() {
        return phase;
    }

    /**
     * @param phase the phase to set
     */
    public void setPhase(FinalPhase phase) {
        this.phase = phase;
    }

}