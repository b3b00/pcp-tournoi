package org.pcp.tournament.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Player implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private Boolean isLicensed;

    @JsonIgnore
    @ManyToOne //(cascade = CascadeType.)
    private Tournament tournament;
    
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public String toString() {
        return getName()+" "+(getIsLicensed()?"X":"");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Player) {
                Player p2 =(Player)obj;
                return getName() == p2.getName() && getIsLicensed() == p2.getIsLicensed();

            }            
        }
        return false;
        
    }

    @Override
    public int hashCode() {
        return (getName()+getIsLicensed()).hashCode();
    }
}