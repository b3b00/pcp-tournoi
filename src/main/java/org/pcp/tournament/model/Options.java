package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Options {

    @Id
    @GeneratedValue
    private int id;

    private String label;

    private Mode mode;

    private int winningSets;

    private int setLength;

    private Boolean isPreset;

    public Options() {
        isPreset = false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Options(Mode mode, String label, int winningSets, int setLength) {
        setMode(mode);
        setWinningSets(winningSets);
        setSetLength(setLength);       
        setIsPreset(false); 
        setLabel("");
    }

    public Options(Mode mode, String label, int winningSets, int setLength, Boolean isPreset) {
        setMode(mode);
        setWinningSets(winningSets);
        setSetLength(setLength);        
        setIsPreset(isPreset);
        setLabel(label);
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

    /**
     * @return the isPreset
     */
    public Boolean getIsPreset() {
        return isPreset;
    }

    /**
     * @param isPreset the isPreset to set
     */
    public void setIsPreset(Boolean isPreset) {
        this.isPreset = isPreset;
    }

}