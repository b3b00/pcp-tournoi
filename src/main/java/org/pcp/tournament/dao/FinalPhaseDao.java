package org.pcp.tournament.dao;


import org.pcp.tournament.model.FinalPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalPhaseDao extends JpaRepository<FinalPhase, Integer> {

    public FinalPhase findById(int id);
    
}