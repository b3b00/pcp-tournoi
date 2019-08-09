<style>    
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import SelectableUL from './selectableUL.svelte';

    const dispatch = createEventDispatcher();

    let teams = [];

    export let tournamentId = -1;

    export let team = {};

    export let selected = false;

    

    onMount(async () => {
       
        console.log(team);
    });

    function unTeam(team, player) {
        dispatch("unteam", { 'team':team,'player':player });
    }

    function selectTeam(state) {
        selected=state.detail.state;
        dispatch("selectionChanged",state);
    }
</script>

{#if (team.player1 != null || team.player2 != null)}
<SelectableUL on:selectionChanged={selectTeam} selected={selected} >


    {#if (team.player1 != null)}
    <li class="w3-display-container">
        {team.player1.name} {team.player1.isLicensed ? "X" : ""}
        <span on:click="{() => {unTeam(team,team.player1)}}" class="w3-button w3-display-right">&times;</span>        
    </li>
    {/if}
    {#if (team.player2 != null)}
    <li class="w3-display-container">
        {team.player2.name} {team.player2.isLicensed ? "X" : ""}
        <span on:click="{() => {unTeam(team,team.player2)}}" class="w3-button w3-display-right">&times;</span>
    </li>
    {/if}
</SelectableUL>
{/if}
