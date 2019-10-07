<style type="text/scss">
    @import "../../styles/global.scss";
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

    export let unTeamAble = true;

    onMount(async () => {
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
<SelectableUL on:selectionChanged={selectTeam} selected={selected} on:drop payload={team}>


    {#if (team.player1 != null)}
    <li class="w3-display-container">
        {team.player1.name} <span class={team.player1.isLicensed ? "fa fa-star w3-display-center" : ""}></span>
        {#if unTeamAble}
        <span on:click="{() => {unTeam(team,team.player1)}}" class="w3-button w3-display-right">&times;</span>        
        {/if}
    </li>
    {/if}
    {#if (team.player2 != null)}
    <li class="w3-display-container">
        {team.player2.name} <span class={team.player2.isLicensed ? "fa fa-star w3-display-center" : ""}></span>
        {#if unTeamAble}
        <span on:click="{() => {unTeam(team,team.player2)}}" class="w3-button w3-display-right">&times;</span>
        {/if}
    </li>
    {/if}
</SelectableUL>
{/if}
