package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class TournamentBoard {


    @Id
    @GeneratedValue
    private int id;

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

    @OneToMany
    private List<FinalPhase> boards;

    /**
     * @return the rounds
     */
    public List<FinalPhase> getBoards() {
        return boards;
    }

    /**
     * @param rounds the rounds to set
     */
    public void setBoards(List<FinalPhase> boards) {
        this.boards = boards;
    }

}