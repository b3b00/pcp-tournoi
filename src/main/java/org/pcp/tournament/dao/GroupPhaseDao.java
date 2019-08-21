package org.pcp.tournament.dao;


import org.pcp.tournament.model.GroupPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPhaseDao extends JpaRepository<GroupPhase, Integer> {

    public GroupPhase findById(int id);
    
}