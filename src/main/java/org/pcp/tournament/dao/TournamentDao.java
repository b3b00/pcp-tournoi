package org.pcp.tournament.dao;

import java.util.List;

import org.pcp.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentDao extends JpaRepository<Tournament, Integer> {

    public Tournament findById(int id);

    public Tournament findByName(String name);

    public List<Tournament> findByOwner(String owner);

}