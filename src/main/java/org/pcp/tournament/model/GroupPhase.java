package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name="group_phase")
public class GroupPhase {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "phase")
    private List<GroupPlay> groups;

    public GroupPhase() {
        groups = new ArrayList<GroupPlay>();
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
    * @return the groups
    */
   public List<GroupPlay> getGroups() {
       return groups;
   }

   /**
    * @param groups the groups to set
    */
   public void setGroups(List<GroupPlay> groups) {
       this.groups = groups;
   }

   public void addGroupPlay(GroupPlay groupPlay) {
       this.groups.add(groupPlay);
   }
    


}