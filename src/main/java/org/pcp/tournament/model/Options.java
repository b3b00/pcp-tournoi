package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Options {

    private Mode mode;

    @Id
    @GeneratedValue
    private int id;

    private int winningSets;

    private int setLength;

    public Options(Mode mode, int winningSets, int setLength) {
        setMode(mode);
        setWinningSets(winningSets);
        setSetLength(setLength);        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }
    /**
     * @param mode the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * @return the winningSets
     */
    public int getWinningSets() {
        return winningSets;
    }

    /**
     * @param winningSets the winningSets to set
     */
    public void setWinningSets(int winningSets) {
        this.winningSets = winningSets;
    }

    /**
     * @return the setLength
     */
    public int getSetLength() {
        return setLength;
    }

    /**
     * @param setLength the setLength to set
     */
    public void setSetLength(int setLength) {
        this.setLength = setLength;
    }

}