package org.pcp.tournament.dao;

import org.pcp.tournament.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerDao extends JpaRepository<Player, Integer> {

    public Player findById(int id);

}