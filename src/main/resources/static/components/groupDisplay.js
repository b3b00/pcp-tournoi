export const GroupDisplay = {

  getPlayerDisplayName: function(player) {
    const licenseeStar = "<span class='fa fa-star w3-display-center'></span>";
    let name = "";
    if (player !== null && player !== undefined) {
      name = player.name;
      if (player.isLicensed) {
        name = name + "    " + licenseeStar;
      }
    }
    return name;
  },

  getTeamDisplayName: function(team, referenceLabel) {
    let name = "";
    if (team != null && team !== undefined) {
      if (team.player2 != null) {
        let name1 = this.getPlayerDisplayName(team.player1);
        let name2 = this.getPlayerDisplayName(team.player2);
        name = name1 + " <br/> " + name2;
      } else {        
        name = this.getPlayerDisplayName(team.player1);
      }
    }
    else {
      if (referenceLabel != null && referenceLabel !== undefined) {
        name = referenceLabel;
      }
    }
    
    return name;
  }
};
