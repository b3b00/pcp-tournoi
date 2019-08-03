package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@Entity
public class Player {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private Boolean isLicensed;

    
    
    public Player() {

    }

    public Player(String playerName, Boolean licensed) {
        setName(playerName);
        setIsLicensed(licensed);
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isLicensed
     */
    public Boolean getIsLicensed() {
        return isLicensed;
    }

    /**
     * @param isLicensed the isLicensed to set
     */
    public void setIsLicensed(Boolean isLicensed) {
        this.isLicensed = isLicensed;
    }

    @Override
    public String toString() {
        return getId()+" - "+getName()+" "+(getIsLicensed()?"X":"");
    }
}