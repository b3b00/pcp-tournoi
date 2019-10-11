package org.pcp.tournament.web;

public class Identity {

    private String Id;

    private boolean Ok;

    private String Name;

    public Identity() {
        Ok=false;
    }

    public Identity(String id, String name) {
        Id = id;
        Name = name;
        Ok = true;
    }

    public static Identity Fail() {
        return new Identity();
    }

    /**
     * @return the id
     */
    public String getId() {
        return Id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        Id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return Name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        Name = name;
    }

    public boolean IsOk() {
        return Ok;
    }
    

}