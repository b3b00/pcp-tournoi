package org.pcp.tournament.dao;


import org.pcp.tournament.model.TournamentBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentBoardDao extends JpaRepository<TournamentBoard, Integer> {

    public TournamentBoard findById(int id);
    
}