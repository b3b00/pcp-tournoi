package org.pcp.tournament.dao;

import org.pcp.tournament.model.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunDao extends JpaRepository<Run, Integer> {

    public Run findById(int id);

}