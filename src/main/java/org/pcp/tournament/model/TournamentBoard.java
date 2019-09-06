package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;


@Entity
public class TournamentBoard implements IPingModel  {


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

    @OneToMany(cascade=CascadeType.REMOVE)
    private List<FinalPhase> boards;


    public TournamentBoard() {
        boards = new ArrayList<FinalPhase>();
    }

    /**
     * @return the rounds
     */
    public List<FinalPhase> getBoards() {
        return boards;
    }

    public FinalPhase getBoard(String name) {
        return boards.stream().filter((FinalPhase fp) -> fp.getName().equals(name))
            .findAny()
            .orElse(null);
    }

    public void addBoard(FinalPhase board) {
        this.boards.add(board);
    }

    /**
     * @param rounds the rounds to set
     */
    public void setBoards(List<FinalPhase> boards) {
        this.boards = boards;
    }

	public void computeScores(Options options) {
        for (FinalPhase board : boards) {
            board.computeScores(options);
        }
	}

}