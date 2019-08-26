
<script>
        
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


    import Team from './team.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const modes = {
        RANDOM: "RANDOM",
        MIX: "MIX",
        SINGLE : "SINGLE"
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
            if (tournament != null && tournament.options.mode == "SINGLE") {
                single();
            }
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
        unTeamedPlayers = substract(tournament.players, teamed);
    }

    function substract(a, b) {    
        let res = a.filter(aa => !b.find(bb => bb.id == aa.id));
        return res;
    }

    async  function single() {
        computeTeams(modes.SINGLE)
    }

    async function random() {
        computeTeams(modes.RANDOM);
    }

    async function mix() {
        computeTeams(modes.MIX);
    }

    async function clear() {
        const uri = `/tournaments/${tournamentId}/teams`;        
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
        const uri = `/tournaments/${tournamentId}/teams/$create?mode=${mode}`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        });
        tournament = await res.json();
        computeUnTeamedPlayers(); // for real
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
                        computeUnTeamedPlayers(); 
                    }
                }
            });
            tournament.teams = tournament.teams;
            computeUnTeamedPlayers();
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
            computeUnTeamedPlayers();           
    }

    function isTeamEmpty(team) {
        return (team.player1 == null && team.player2 == null);
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
            computeUnTeamedPlayers();
            tournament = tournament;
        }
        else if (selectedPlayers.length == 1 && selectedTeams.length == 1) {
            addPlayerToTeam();
            clearSelection();
            computeUnTeamedPlayers();
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
            computeUnTeamedPlayers();
    }




</script>

{#if (tournament !== null && tournament !== undefined &&
    tournament.options !== null && tournament.options !== undefined && 
    tournament.options.mode == "DOUBLE")}
<br/>
<hr/>
<br/>
<button on:click={random}>hasard total</button>

<button on:click={mix}>mixe</button>

<button on:click={clear}>tout supprimer</button>

<div>

<!-- équipes -->    
<div class="w3-container w3-cell" style="width:60%">
{#if (tournamentId != -1 && tournament.teams !==  undefined && tournament.teams !== null && tournament.teams.length > 0)} 
    <ul class="w3-ul w3-border w3-card">
    {#each tournament.teams as team}
        {#if (!isTeamEmpty(team))}
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
   
    <button on:click={buildTeam} class="fa fa-arrow-left">        
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
                            style={player.selected ? "background-color:lightgray;" : "background-color:white"}>            
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
        {#if (tournamentId != -1 && tournament.teams !==  undefined && tournament.teams !== null && tournament.teams.length > 0)} 
        {#each tournament.teams as team}
        <li>            
            <span>{team.name}</span>
        </li>
        {/each} 
        {/if}      
    </ul>
</div>
{/if}
{/if}


