<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let tournament = {};
    let players = [];
    export let tournamentId = -1;

    onMount(async () => {
            await load();
        });

    async function load() {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json();        
        players = tournament.players;
    }

    function backtoconfig() {
        dispatch("back", { 'tournamentId': tournamentId })
    }

    function toTeams() {
        dispatch("next", { 'tournamentId': tournamentId })
    }



</script>
<button on:click={backtoconfig} >back to config</button>
<button on:click={toTeams} >teams</button>
<br/>
<p>{tournament.name} - {tournament.id}</p>
<table class="w3-table-all w3-centered w3-card-4 w3-small " style="width: 50%;margin: auto;">
<tr>
    <th>nom</th>
    <th>licencié ?</th>
    <th>&nbsp;</th><!-- delete -->
    <th>&nbsp;</th><!-- save -->
    <th>&nbsp;</th><!-- cancel -->
</tr>
<tr>
<Player isNewPlayer={true} name="" id="" isLicensed="false" tournamentId={tournamentId} on:change={load} edited={false}></Player>
</tr>
{#each players as p}
<tr>
    <Player {...p} tournamentId={tournamentId} on:change={load} isNewPlayer={false} edited={false}/>
</tr>


{/each}
</table>
