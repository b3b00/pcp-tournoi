package org.pcp.tournament.dao;

import org.pcp.tournament.model.Mode;
import org.pcp.tournament.model.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OptionsDao extends JpaRepository<Options, Integer> {

    public Options findById(int id);

    public Options findByMode(Mode mode);

    public List<Options> findAllByIsPreset(Boolean isPreset);
    
}