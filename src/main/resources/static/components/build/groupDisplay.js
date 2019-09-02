
export const GroupDisplay = {
  
    

    getPlayerDisplayName : function(player) {
    const licenseeStar = "<span class='fa fa-star w3-display-center'></span>"
      let name = player.name;
        if (player.isLicensed) {
            name = name + "    " + licenseeStar;
        }
      return name;
  },

  getTeamDisplayName : function(team) {
      let name = "";
    if (team.player2 != null) {                    
        let name1 = this.getPlayerDisplayName(team.player1);
        let name2 = this.getPlayerDisplayName(team.player2);
        name = name1 + " - "+name2;
    }
    else {
        name = this.getPlayerDisplayName(team.player1);
    }
    return name;
  }
  

}