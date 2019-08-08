<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let teams = [];

    export let tournamentId = -1;

    export let team = {};

    onMount(async () => {
        console.log(team);
    });

    function unTeam(team, player) {
        dispatch("unteam", { 'team':team,'player':player });
    }

</script>


<ul class="w3-ul w3-border w3-card" >
    <li class="w3-display-container">
        {team.player1.name} {team.player1.isLicensed ? "X" : ""}
        <span on:click="{() => {unTeam(team,team.player1)}}" class="w3-button w3-display-right">&times;</span>        
    </li>
    <li class="w3-display-container">
        {team.player2.name} {team.player2.isLicensed ? "X" : ""}
        <span on:click="{() => {unTeam(team,team.player1)}}" class="w3-button w3-display-right">&times;</span>
    </li>
</ul>
