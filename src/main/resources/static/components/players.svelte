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
<table class="w3-table-all w3-centered w3-card-4 w3-small " style="width: 50%;margin: auto;">
<tr>
    <th>nom</th>
    <th>licencié ?</th>
    <th>&nbsp;</th>
</tr>
<Player isNewPlayer="true" name="" id="" isLicensed="false"/>
{#each players as p}
<tr>
    <Player {...p} tournamentId={tournamentId}/>
</tr>


{/each}
</table>
