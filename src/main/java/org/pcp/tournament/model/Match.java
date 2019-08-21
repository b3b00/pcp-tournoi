package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.List;
import java.util.ArrayList;


@Entity
public class Match {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne    
    private Team leftTeam;

    @OneToOne
    private Team rightTeam;

    @OneToMany
    private List<MatchSet> score;

    public Match() {
        score = new ArrayList<MatchSet>();
    }

    public int getId() {
        return id;
    }

    public Team getLeft() {
		return leftTeam;
	}

	public void setLeft(Team left) {
		this.leftTeam = left;
	}

	public Team getRight() {
		return rightTeam;
	}

	public void setRight(Team right) {
		this.rightTeam = right;
	}

	public void setId(int id) {
        this.id = id;
    }

    public List<MatchSet> getScore() {
        return this.score;
    }

    public void setScore(List<MatchSet> sets) {
        this.score = sets;
    }

    public void addSet(MatchSet set) {
        this.score.add(set);
    }

    public MatchSet getSet(int i) {
        return this.score.get(i);
    }


    
}