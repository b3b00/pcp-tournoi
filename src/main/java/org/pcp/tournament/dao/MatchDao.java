package org.pcp.tournament.dao;

import org.pcp.tournament.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchDao extends JpaRepository<Match, Integer> {

    public Match findById(int id);

}