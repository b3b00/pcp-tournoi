export const modes = {
    RANDOM: "RANDOM",
    MIX: "MIX",
    SINGLE : "SINGLE"
}

export const tools = {
    substract : function (a, b) {    
        let res = a.filter(aa => !b.find(bb => bb.id == aa.id));
        return res;
    },

    computeUnTeamedPlayers :  function (tournament) {
        let unTeamedPlayers = [];
        let teamed = [];
        if (tournament.teams !== undefined && tournament.teams !== null) {
        tournament.teams.forEach(t => {
            if (t.player1 != null) {
                teamed.push(t.player1);
            }
            if (t.player2 != null) {
                teamed.push(t.player2);
            }
        });
        }
        unTeamedPlayers = this.substract(tournament.players, teamed);
        return unTeamedPlayers;
    },

    clear : async function (tournamentId) {
        const uri = `/tournaments/${tournamentId}/teams`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "DELETE"
        });
        let tournament = await res.json();
        return tournament;
    },

    computeTeams : async function (mode,tournamentId) {
        const uri = `/tournaments/${tournamentId}/teams/$create?mode=${mode}`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        });
        let tournament = await res.json();
        return tournament;
    },


}