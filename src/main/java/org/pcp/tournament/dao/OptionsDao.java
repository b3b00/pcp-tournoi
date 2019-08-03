package org.pcp.tournament.dao;

import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.pcp.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsDao extends JpaRepository<Options, Integer> {

    public Options findById(int id);

    public Options findByMode(Mode mode);
    
}