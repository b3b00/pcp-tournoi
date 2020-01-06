import { createEventDispatcher } from "svelte";
import { alertError } from './alertStore.js';

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
  },

  deleteTournament : async function(tournamentId) {
    const res = await fetch(`/tournament/${tournamentId}`,{
      headers: {      
      },
      method: "DELETE" 
    }
    );
    let tournaments = await res.json();
    return tournaments;
  },

  fetchGroupPhase : async function(groupPhaseId) {
    const res = await fetch(`/groupPhase/${groupPhaseId}`);
    let phase = await res.json();
    return phase;
  }
  ,
  fetchGroupPlay : async function(groupPlayId) {
    const res = await fetch(`/groupPlay/${groupPlayId}`);
    let group = await res.json();
    return group;
  },

  fetchRound : async function(tournamentId, roundId) {
    const res = await fetch(`/tournament/${tournamentId}/round/${roundId}`);
    let round = await res.json();
    return round;
  },

  fetchBoard : async function(tournamentId, boardId) {
    const res = await fetch(`/tournament/${tournamentId}/board/${boardId}`);
    let board = await res.json();
    return board;
  },

  fetchAvailableTeams : async function(tournamentId) {
    const res = await fetch(`/tournament/${tournamentId}/availableTeams`);
    let teams = await res.json();
    return teams;
  },

  createBoardWithTeams : async function(tournamentId, teams, name) {
    const res = await fetch(`/tournament/${tournamentId}/$createBoard?name=${name}`,
    {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: "POST",
      body: JSON.stringify(teams)
    });
    if (res.status >= 200 && res.status <= 299) {
      let tournament = await res.json();
      return tournament;
    }
    else {
      const body = await res.body;
      alertError(`Erreur lors de la création du tournoi : ${res.status}<br/>${body}`);
      
      return await this.fetchTournament(tournamentId);
    }
  },

  notNullOrUndefined : function(o)  {
    return o !== undefined && o !== null;
  },

  notEmpty: function(o) {
    return Array.isArray(o)  && o.length > 0;
  },

  checkItem : function(root, path) {
    const checkLength = path[path.length-1] == "#";
    if (checkLength) {
      path = path.slice(0,-1);
    }
    console.log("\tcheck item ["+path+"] length : ["+checkLength+"]");
    console.log(root);
    let ok = true;
    let item = null;
    if (root.hasOwnProperty(path)) {
      item = this.getProp(root,path);
      ok = this.notNullOrUndefined(item);
      console.log(item);
      if (ok) {
        if (checkLength) {
          ok = this.notEmpty(item);
          //console.log("\t  length check. "+ok);
        }

      }
      else {
        //console.log("\t failed on not null or undefined.")
      }
    }
    else {
      //console.log("\t failed on hasownproperty.")
      ok = false;
    }
    //console.log(`\t OK:[${ok}] - `)
    return {
      ok : ok,
      item : item
    };

  },

  getProp: function(root, item) {
    console.log("GETPROP : ["+item+"]");
    for (const key in root) {
      console.log("\t comparing "+key+" - "+item);
      if (root.hasOwnProperty(key) && key == item) {
        const element = root[key];
        return element;
     }
    }
    return undefined;
  },

  guard : function(root, toBeGuarded) {
    // console.log("GUARD ["+toBeGuarded+"]");
    console.log(root);
    
    if (this.notNullOrUndefined(root));
    let items = toBeGuarded.split(".");
    let ok = true;
    let currentRoot = root;
    for (let i =0 ; i < items.length; i++) {
      if (!ok) {
        return false;
      }
      // console.log("----");
      // console.log(currentRoot);
      // console.log("----");
      const currentCheck = this.checkItem(currentRoot,items[i]);
       ok = ok && currentCheck.ok;
       currentRoot = currentCheck.item;
    }

    return ok;
  }

}
