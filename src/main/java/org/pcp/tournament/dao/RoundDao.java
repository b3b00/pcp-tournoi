package org.pcp.tournament.dao;


import org.pcp.tournament.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundDao extends JpaRepository<Round, Integer> {

    public Round findById(int id);
    
}