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
        countLic =  await countLicensees();
        countNonLic = await countNotLicensees()
    }

    let countLic = 0;
    let countNonLic = 0;

    async function countLicensees() {
        let count = 0;
        players.forEach(p => {
            if (p.isLicensed) {
                count ++;
            }
        });
        return count;
    }


    
    async function countNotLicensees() {
        return  players.length - countLic;

    }


</script>
<p>{players.length} joueurs</p>
<p>{countLic} licenciés</p>
<p>{countNonLic} non licenciés</p>
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
