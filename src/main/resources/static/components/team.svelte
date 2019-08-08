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

</script>

<ul class="w3-ul w3-border w3-card" style="width:30%">
    <li class="w3-bar">{team.player1.name} {team.player1.isLicensed ? "X" : ""}</li>
    <li class="w3-bar">{team.player2.name} {team.player2.isLicensed ? "X" : ""}</li>
</ul>