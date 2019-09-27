package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Round implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Match> matches;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private FinalPhase phase;

    private boolean isFinal;

    @Transient
    private List<Match> matchGroups;

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

    public boolean isDone() {
        for (Match match : matches) {
            if (!match.getIsEnded()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the matchGroups
     */
    public List<List<Match>> getMatchGroups() {
        List<List<Match>> groups = new ArrayList<List<Match>>();
        int groupCount = (int)(Math.ceil((double)matches.size()/2.0));
        for (int i = 0; i < groupCount; i++) {
            List<Match> group = new ArrayList<Match>();
            group.add(matches.get(i*2));
            if (matches.size() > i*2+1) {
                group.add(matches.get(i*2+1));
            }
            groups.add(group);
        }
        return groups;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Match match : matches) {
            builder.append(match.toString());
            builder.append("\n");            
        }
        return builder.toString();
        
    }

	public void computeScores(Options options) {
        for (Match match : matches) {
                match.compute(options);
        }
	}

}