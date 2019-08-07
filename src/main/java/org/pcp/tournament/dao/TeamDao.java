package org.pcp.tournament.dao;

import org.pcp.tournament.model.Player;
import org.pcp.tournament.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamDao extends JpaRepository<Team, Integer> {

    public Team findById(int id);

    @Query("SELECT t FROM Team t WHERE t.player1 = :player OR t.player2 = :player")
    public Team findWithPlayer(@Param("player") Player player);

}