package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Round implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany
    private List<Match> matches;

    @ManyToOne
    @JsonIgnore
    private FinalPhase phase;

    private boolean isFinal;

    public Round() {
        matches = new ArrayList<Match>();
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
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

    public void addMatch(Match match) {
        this.matches.add(match);
    }


    public int getName() {
        return matches.size();
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