<script>

    import Team from './team.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const modes = {
        RANDOM: "RANDOM",
        MIX: "MIX"
    }

    const dispatch = createEventDispatcher();

    export let tournamentId = -1;

    let tournament = {};

    let groupNumber = 8;

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
    }

</script>

<div class="w3-panel w3-card startDialog" >




    <label for="name">Nombre de poules : </label>
    <input type="text" name = "number" id="number" class="w3-input" bind:value={groupNumber}/>

</div>
