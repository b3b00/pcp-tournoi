package org.pcp.tournament.service;

import org.pcp.tournament.dao.MatchDao;
import org.pcp.tournament.dao.MatchSetDao;
import org.pcp.tournament.model.Match;
import org.pcp.tournament.model.MatchSet;
import org.pcp.tournament.model.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MatchService {

    @Autowired
    public MatchDao matchDao;

    @Autowired
    public MatchSetDao matchSetDao;

    public Match createMatch(Match match, Options options) {
            Match newMatch = matchDao.save(match);
            int setCount = options.getWinningSets() * 2 - 1;
            for (int i = 0; i < setCount; i++) {
                MatchSet set = new MatchSet();
                set.setLeft(0);
                set.setRight(0);
                set.setMatch(match);
                set = matchSetDao.save(set);
                newMatch.addSet(set);
            }
            newMatch = matchDao.save(match);
            return newMatch;
    }

}