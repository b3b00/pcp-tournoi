
<style type="text/scss">
@import "../../styles/global.scss";
</style>
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
  });




  async function refresh() {
    dispatch("refresh",{});
  }

</script>



<div class="w3-container w3-third">
    {#if round !== null && round !== undefined}
    {#if (round.final)}
    <p style="text-align: center; font-weight: bold;">Finale</p>
    {:else}
    <p style="text-align: center; font-weight: bold;">1&nbsp;/&nbsp;{round.matches.length}</p>    
    {/if}
    {#each round.matchGroups as group }
    <div class="w3-container w3-card">
      {#each group as g}
        <MatchPreview on:refreshTour match={g} tournament={tournament} on:move on:matchSaved={refresh}/>
      {/each}
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
