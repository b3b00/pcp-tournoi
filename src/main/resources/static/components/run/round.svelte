
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
    tournament = await tools.fetchTournament(id);
  });




  function refresh() {

  }

</script>



<div class="w3-container">
    {#if round !== null && round !== undefined}
    {#if (round.final)}
    <h1>Finale</h1>
    {:else}
    <h1>1&nbsp;/&nbsp;{round.matches.length}</h1>
    {/if}
  
    {#each round.matches as match }    
    <MatchPreview match={match} tournament={tournament} on:move on:matchSaved={refresh}/>
  {/each}
  {/if}

</div>
