<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let players = [];
    export let tournamentId = -1;

    onMount(async () => {
            await load();
        });

    async function load() {
        const res = await fetch(`/tournament/${tournamentId}/players`);
        players = await res.json();        
    }

</script>
<table class="w3-table-all w3-centered w3-card-4 w3-small " style="width: 50%;margin: auto;">
<tr>
    <th>nom</th>
    <th>licencié ?</th>
    <th>&nbsp;</th><!-- delete -->
    <th>&nbsp;</th><!-- save -->
    <th>&nbsp;</th><!-- cancel -->
</tr>
<tr>
<Player isNewPlayer="true" name="" id="" isLicensed="false" tournamentId={tournamentId} on:change={load}></Player>
</tr>
{#each players as p}
<tr>
    <Player {...p} tournamentId={tournamentId} on:change={load} isNewPlayer="false"/>
</tr>


{/each}
</table>
