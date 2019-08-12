package org.pcp.tournament.dao;


import org.pcp.tournament.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao extends JpaRepository<Group, Integer> {

    public Group findById(int id);

    public Group  findByName(String name);


    // @Query("SELECT g FROM Group t WHERE :player MEMBER OF t.teams ")
    // public Group findWithTeam(@org.springframework.data.repository.query.Param("team") Team team);
    
}