import { createEventDispatcher } from "svelte";

export const tools = {
  mover : function (dispatch) {
      return function(name, label, path, id) {
        //const dispatch = createEventDispatcher();
      dispatch("move", {
        "name": name,
        "label": label,
        "path": path,
        "id": id
      });
    }
  }
  ,
  fetchTournament : async function(tournamentId) {
    const res = await fetch(`/tournaments/${tournamentId}`);
    let tournament = await res.json();
    return tournament;
  }
  ,
  fetchGroupPlay : async function(groupPlayId) {
    const res = await fetch(`/groupPlay/${groupPlayId}`);
    let group = await res.json();
    return group;
  }

}
