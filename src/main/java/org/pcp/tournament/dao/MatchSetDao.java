package org.pcp.tournament.dao;

import org.pcp.tournament.model.MatchSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchSetDao extends JpaRepository<MatchSet, Integer> {

    public MatchSet findById(int id);

}