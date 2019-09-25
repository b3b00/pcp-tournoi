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
    let tournament = await res.json();
    return tournament;
  }

}
