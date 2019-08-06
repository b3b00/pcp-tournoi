<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let players = [];
    export let tournamentId = -1;

    onMount(async () => {
            const res = await fetch(`/tournament/${tournamentId}/players`);
            players = await res.json();        
        });

</script>

<ul>
{#each players as p}
<li class="w3-bar">
    <Player {...p} tournamentId={tournamentId}/>
</li>

{/each}

</ul>