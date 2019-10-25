<style type="text/scss">
    @import "../../styles/global.scss";

    .selected {
        @extend .pcp-color1;
    }

    .notselected {
        background-color: white;
    }

</style>
<script>

    import Team from './team.svelte';
    import { alertWarn } from '../alertStore.js';
    import { tools } from '../tools.js';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import {teamtools, modes} from './teams.js';
        
    function dragstartPlayer (player) {
        return function(ev) {

            ev.dataTransfer.setData("application/json", JSON.stringify(player));
        }
    }
    
    function dropPlayerOnTeam (ev) {
            ev.preventDefault();
            let pjson = ev.detail.event.dataTransfer.getData('application/json');                        
            let player = JSON.parse(pjson);                        
            let realteam = ev.detail.payload;
            if (realteam.player1 == null || realteam.player2 == null) {
                addPlayer(realteam,player);
            }
    }

    function dropPlayerOnPlayer (sourceplayer) {
        return function(ev) {
            ev.preventDefault();
            let pjson = ev.dataTransfer.getData('application/json');                        
            let targetplayer = JSON.parse(pjson);                        
            createTeam(sourceplayer,targetplayer);
        }
    } 

    const dispatch = createEventDispatcher();

    let teams = [];
    export let tournamentId = -1;

    let unTeamedPlayers = [];

    let teamed = [];

    let tournament = {};

    let possible = true;

    onMount(async () => {
        tournament = {
            id: tournamentId,
            teams: [],
            players: []
        };

        if (tournamentId != -1) {
            await load(); 
            if (tournament.options.mode == "SINGLE" && (tournament.teams === undefined || tournament.teams === null || tournament.teams.length == 0)) {
                single();
            }
            else {
                if (!(tournament !== null && tournament !== undefined && tournament.players !== undefined && tournament.players.length > 0 && tournament.players.length % 2 == 0)) {                 
                    alertWarn(`nombre de joueurs impair : ${tournament.players.length} `);
                    possible = false;
                }
            }
        }

    });



    async function load() {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json();
        if (tournament !== undefined && tournament !== null) {
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
        }
    }

    

   

    async  function single() {
        tournament = await teamtools.computeTeams(modes.SINGLE, tournament.id);
        unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }

    async function random() {
        tournament = await teamtools.computeTeams(modes.RANDOM, tournament.id);
        unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }

    async function mix() {
        tournament = await teamtools.computeTeams(modes.MIX, tournament.id);
        unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }

    async function clear() {
        tournament = await teamtools.clear(tournament.id);        
        unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }

    

    function onUnTeam(data) {
        let team = data.detail.team;
        let player = data.detail.player;
        unTeam(team, player);
    }

    async function unTeam(team, player) {
        if (team != null && player != null) {
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
                        const uri = `/tournaments/${tournamentId}/teams/${team.id}/$update`;        
                        const res = await fetch(uri, {
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            method: "PUT",
                            body:JSON.stringify(t)
                        });
                        tournament = await res.json();
                        unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
                    }
                }
            });
            tournament.teams = tournament.teams;
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
        }
    }

    async function createTeam(player1, player2 ) {
        const uri = `/tournaments/${tournament.id}/teams?player1=${player1.id}&player2=${player2.id}`;
        const res = await fetch(uri,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST"
            });
            tournament = await res.json();
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }

    
    

    function selectUnteamedPlayer(player) {
        let realp = unTeamedPlayers.find(p => p.id == player.id);
        realp.selected = !realp.selected;
        unTeamedPlayers = unTeamedPlayers;
    }

    function selectTeam(team, data) {
        team.selected = data.detail.state;
        tournament.teams.forEach(t => {
            if (t.id == team.id) {
                t.selected = data.detail.detail.state;
            }
            else {
                if (t.selected === undefined) {
                    t.selected = false;
                }
            }
        })
    }

    function teamPlayers() {
        let selectedPlayers = unTeamedPlayers.filter(p => p.selected);
        if (selectedPlayers.length == 2) {
            createTeam(selectedPlayers[0],selectedPlayers[1]);
        }
        else {
            alert("vous devez sélectionner 2 joueurs.");
        }
    }

    function clearSelection() {
        unTeamedPlayers.forEach(p => p.selected = false)
        tournament.teams.forEach(t => t.selected=false);
    }

    async function buildTeam() {
        let selectedPlayers = unTeamedPlayers.filter(p => p.selected);
        let selectedTeams = tournament.teams.filter(t => t.selected);
        if (selectedPlayers.length == 2) {
            await teamPlayers();
            clearSelection();
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
            tournament = tournament;
        }
        else if (selectedPlayers.length == 1 && selectedTeams.length == 1) {
            addPlayerToTeam();
            clearSelection();
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
        }
        else {
            alert(`vous devez sélectionner soit 
                - soit 2 joueurs 
                - soit 1 joueur et 1 équipe`);
        }

    }

    async function addPlayerToTeam() {
        let selectedPlayers = unTeamedPlayers.filter(p => p.selected);
        let selectedTeams = tournament.teams.filter(t => t.selected);
        if (selectedPlayers.length == 1 && selectedTeams.length == 1) {
            const team = selectedTeams[0];
            const player = selectedPlayers[0];
            addPlayer(team,player);
        }
        else {
            alert("vous devez sélectionner 1 joueur et 1 équipe.");
        }
    }

    async function addPlayer(team, player) {
        const uri = `/tournaments/${tournament.id}/teams/${team.id}?player=${player.id}`;
            const res = await fetch(uri,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "PUT"
            });
            tournament = await res.json();
            unTeamedPlayers = teamtools.computeUnTeamedPlayers(tournament);
    }




</script>


{#if (tools.guard(tournament,"options") && 
    tournament.options.mode == "DOUBLE" && possible)}
<br/>
<hr/>
<br/>
<button on:click={random}>hasard total</button>

<button on:click={mix}>1 confirm&eacute; - 1 d&eacute;butant</button>

<button on:click={clear}>tout supprimer</button>

<div>

<!-- équipes -->    
<div class="w3-container w3-cell" style="width:60%">
{#if (tournamentId != -1 && tournament.teams !==  undefined && tournament.teams !== null && tournament.teams.length > 0)} 
    <ul class="w3-ul w3-border w3-card">
    {#each tournament.teams as team}
        {#if (!teamtools.isTeamEmpty(team))}
            <li class="w3-display-container">        
                <Team on:drop={dropPlayerOnTeam} team={team} on:unteam={onUnTeam} selected={team.selected} on:selectionChanged={(data) => { selectTeam(team,data) }}/>       
            </li>
        {/if}
    {/each}
    </ul>
{:else}
    <p>no teams</p>
{/if}
</div>
{#if (unTeamedPlayers.length > 0)}
<!-- action sur les équipes et joueurs -->
<div class="w3-container w3-cell" style="width:10%">
   
    <button on:click={buildTeam} class="fa fa-arrow-left awesome">        
    </button>

</div>

<!-- joueurs non affectés -->

<div class="w3-container w3-cell" style="width:60%">
    <ul class="w3-ul w3-border w3-card">
        
        {#each unTeamedPlayers as player (player.id)}
        <li  draggable=true on:dragover={(e) => {e.preventDefault();}} 
                            on:drop={dropPlayerOnPlayer(player)} 
                            on:dragstart={dragstartPlayer(player)} 
                            on:click={() => {selectUnteamedPlayer(player)}} 
                            class={player.selected ? "selected" : "notselected"}>
            <span>{player.name}<span><span class={player.isLicensed ? "fa fa-star w3-display-center" : ""}></span>
        </li>
        {/each}       
    </ul>
</div>
{/if}


</div>

{:else} 

{#if (tournament !== null && tournament !== undefined &&
    tournament.options !== null && tournament.options !== undefined && 
    tournament.options.mode == "SINGLE")}
    <br/>
    <br/>
<strong>mode simple : pas d'&eacute;quipes</strong>
{:else}    

<hr/>
<div class="w3-container w3-cell" style="width:60%">
    <ul class="w3-ul w3-border w3-card">
        {#if (tournamentId != -1 && tools.guard(tournament,"teams#"))} 
        {#each tournament.teams as team }
        <li>            
            <span>{team.name}</span>
        </li>
        {/each} 
        {/if}      
    </ul>
</div>
{/if}
{/if}


