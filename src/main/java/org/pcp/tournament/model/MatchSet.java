package org.pcp.tournament.model;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="set_table")
public class MatchSet implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @Column(name="left_score")
    private int left;

    @Column(name="right_score")
    private int right;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Match match;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

}