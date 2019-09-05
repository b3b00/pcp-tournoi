
<script>

  import { tools } from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';
  import MatchPreview from './match_preview.svelte';



  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  export let round;

  export let roundId;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    console.log(tournament);
    tournament = await tools.fetchTournament(tournamentId);
  });




  function refresh() {

  }

</script>



<div class="w3-container w3-third">
    {#if round !== null && round !== undefined}
    {#if (round.final)}
    <strong>Finale</strong>
    {:else}
    <strong>1&nbsp;/&nbsp;{round.matches.length}</strong>
    {/if}
  
    {#each round.matchGroups as group }
    <div class="w3-container w3-card">
    <MatchPreview match={group[0]} tournament={tournament} on:move on:matchSaved={refresh}/>
    <MatchPreview match={group[1]} tournament={tournament} on:move on:matchSaved={refresh}/>
  </div>

<!--     
    {#if group.finale}
    
    <p>finale</p>
    {/if}
    {#if match.semiFinale}
    <p>petite finale</p>
    {/if}
    <MatchPreview match={match} tournament={tournament} on:move on:matchSaved={refresh}/>
     -->
  {/each}
  {/if}

</div>
