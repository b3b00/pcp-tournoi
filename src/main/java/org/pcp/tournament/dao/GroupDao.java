package org.pcp.tournament.dao;


import org.pcp.tournament.model.TeamGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao extends JpaRepository<TeamGroup, Integer> {

    public TeamGroup findById(int id);

    public TeamGroup  findByName(String name);
    
}