package org.pcp.tournament.dao;


import org.pcp.tournament.model.GroupPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPlayDao extends JpaRepository<GroupPlay, Integer> {

    public GroupPlay findById(int id);
    
}