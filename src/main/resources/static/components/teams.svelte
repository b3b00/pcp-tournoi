<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let teams = [];
    export let tournamentId = -1;

    onMount(async () => {
        if (tournamentId != -1) {
            await load();
        }
    });

    async function load() {
        const res = await fetch(`/tournament/${tournamentId}/teams`);
        teams = await res.json();
    }

</script>