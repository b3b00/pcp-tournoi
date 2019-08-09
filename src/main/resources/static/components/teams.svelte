<script>

    import Team from './team.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const modes = {
        RANDOM: "RANDOM",
        MIX: "MIX"
    }

    const dispatch = createEventDispatcher();

    let teams = [];
    export let tournamentId = -1;

    let unTeamedPlayers = [];

    let teamed = [];

    let tournament = {};

    onMount(async () => {
        tournament = {
            id: tournamentId,
            teams: [],
            players: []
        };

        if (tournamentId != -1) {
            await load(); 
        }

    });



    async function load() {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json();
        if (tournament !== undefined && tournament !== null) {
            computeUnTeamedPlayers();
        }
    }

    async function computeUnTeamedPlayers() {
        unTeamedPlayers = [];
        let teamed = [];
        tournament.teams.forEach(t => {
            if (t.player1 != null) {
                teamed.push(t.player1);
            }
            if (t.player2 != null) {
                teamed.push(t.player2);
            }
        });
        unTeamedPlayers = substract(tournament.players, teamed);
        console.log("teamed : ");
        console.log(teamed);
        console.log("unteamed : ");
        console.log(unTeamedPlayers)
        console.log(" ");
    }

    function substract(a, b) {    
        let res = a.filter(aa => !b.find(bb => bb.id == aa.id));
        console.log("substract");
        console.log(a);
        console.log(b);
        console.log(res);
        return res;
    }



    async function random() {
        computeTeams(modes.RANDOM);
    }

    async function mix() {
        computeTeams(modes.MIX);
    }

    async function clear() {
        const uri = `/tournaments/${tournamentId}/teams/delete`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "DELETE"
        });
        tournament = await res.json();
        computeUnTeamedPlayers();
    }

    async function computeTeams(mode) {
        const uri = `/tournaments/${tournamentId}/teams/create/${mode}`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        });
        tournament = await res.json();
        computeUnTeamedPlayers(); // for real
        console.log(`tournament ${tournamentId}`);
        console.log(tournament);
    }

    function onUnTeam(data) {
        let team = data.detail.team;
        let player = data.detail.player;
        unTeam(team, player);
    }

    async function unTeam(team, player) {
        if (team != null && player != null) {
            console.log(`unteam player ${player.id}-${player.name} from team ${team.id}`);
            tournament.teams.forEach(async t => {
                if (t != null) {
                    let found = false;
                    if (t.player1 != null && t.player1.id == player.id) {
                        t.player1 = null;
                        found = true;
                    }
                    if (t.player2 != null && t.player2.id == player.id) {
                        t.player2 = null;
                        found = true;
                    }
                    if (found) {
                        // TODO : save the team 
                        // PUT /tournaments/{tournamentId}/teams/{teamId}

                        const uri = `/tournaments/${tournamentId}/teams/${team.id}`;        
                        const res = await fetch(uri, {
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            method: "PUT",
                            body:JSON.stringify(t)
                        });
                        tournament = await res.json();
                        computeUnTeamedPlayers(); 
                    }
                }
            });
            tournament.teams = tournament.teams;
            computeUnTeamedPlayers();
        }
    }

    function team(player1, player2 ) {
        // TODO : call endpoint to create a new team
        // POST /tournaments/{tournamentId}/teams/player1/{player1Id}/player2/{player2Id}
    }

    function isTeamEmpty(team) {
        return (team.player1 == null && team.player2 == null);
    }

</script>
<br />
<button on:click={random}>hasard total</button>

<button on:click={mix}>mixe</button>

<button on:click={clear}>tout supprimer</button>



<div>

<!-- équipes -->    
<div class="w3-container w3-cell" style="width:75%">
{#if (tournamentId != -1 && tournament.teams !==  undefined && tournament.teams !== null && tournament.teams.length > 0)} 
    <ul class="w3-ul w3-border w3-card">
    {#each tournament.teams as team}
        {#if (!isTeamEmpty(team))}
            <li class="w3-display-container">        
                <Team team={team} on:unteam={onUnTeam}/>       
            </li>
        {/if}
    {/each}
    </ul>
{:else}
    <p>no teams</p>
{/if}
</div>

<!-- joueurs non affectés -->
<div class="w3-container w3-cell" style="width:50%">
    <ul class="w3-ul w3-border w3-card">
        <li class="w3-display-container">Joueurs</li>
        {#each unTeamedPlayers as player}
        <li class="w3-display-container">
            <input class="w3-check" type="checkbox"/>
            {player.name}
        </li>
        {/each}       
    </ul>
</div>


</div>


