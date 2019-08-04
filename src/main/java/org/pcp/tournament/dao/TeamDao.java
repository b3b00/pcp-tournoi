package org.pcp.tournament.dao;

import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamDao extends JpaRepository<Team, Integer> {

    public Player findById(int id);

}