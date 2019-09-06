package org.pcp.tournament.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.pcp.tournament.model.dto.TeamRanking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Entity
@Table(name="group_phase")
public class GroupPhase implements IPingModel  {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "phase",cascade=CascadeType.REMOVE)
    @OrderBy("id ASC")
    private List<GroupPlay> groups;

    @OneToOne
    @JsonIgnore
    private Run run;

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
    * @return the run
    */
   public Run getRun() {
       return run;
   }

   /**
    * @param run the run to set
    */
   public void setRun(Run run) {
       this.run = run;
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
    

   public List<TeamRanking> getFullRanking() {
    List<TeamRanking> rankings = new ArrayList<TeamRanking>();
    
    for (GroupPlay group : groups) {
        group.computeRanking();
        rankings.addAll(group.getRankings());
    }
    
    Collections.sort(rankings);

    return rankings;
}


}