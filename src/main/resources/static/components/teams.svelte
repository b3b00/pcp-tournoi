<script>

    import Team from './team.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const modes = {
        RANDOM : "RANDOM",
        MIX : "MIX"
    }

    const dispatch = createEventDispatcher();

    let teams = [];
    export let tournamentId = -1;

    let tournament = {}; 

    onMount(async () => {
        tournament = {
            id : tournamentId,
            teams:[],
            players:[]
        } ;

        if (tournamentId != -1) {
            await load();
        }

    });



    async function load() {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json();         
        console.log(`tournament ${tournamentId}`);
        console.log(tournament);       
    }

    

    async function random() {
        computeTeams(modes.RANDOM);
    }

    async function mix() {
        computeTeams(modes.MIX);
    }

    async function computeTeams(mode) {
        const uri = `/tournaments/${tournamentId}/teams/create/${mode}`;
        const res = await fetch(uri,{
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST"
            });
        tournament = await res.json();   
        console.log(`tournament ${tournamentId}`);
        console.log(tournament);
    }

    function unTeam(data) {
		let team = data.detail.team;
        let player = data.detail.player;
        console.log(`unteam player ${player.id}-${player.name} from team ${team.id}`);
	}

</script>
<br/>
<button on:click={random}>hasard total</button>

<button on:click={mix}>mixe</button>


{#if (tournamentId != -1 && tournament.teams !==  undefined && tournament.teams !== null && tournament.teams.length > 0)} 
<div class="w3-container w3-cell" style="width:50%">
<ul class="w3-ul w3-border w3-card">
{#each tournament.teams as team}
<li class="w3-display-container"><Team team={team} on:unteam={unTeam}/></li>
{/each}
</ul>
</div>
<!-- <div class="w3-container w3-cell" style="width:50%">
    <ul class="w3-ul w3-border w3-card">
        <li class="w3-display-container">other</li>
        <li class="w3-display-container">content</li>
    </ul>
</div> -->

{:else}
<p>no teams</p>
{/if}
