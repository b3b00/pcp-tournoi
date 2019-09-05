
<script>

  import { tools } from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';
  import Round from './round.svelte';



  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  export let board;

  export let boardId;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
  });


  

  

</script>
<div class="w3-container">
{#if board !== null && board!== undefined}
{#each board.rounds as round}
<Round round={round} roundId={round.id} tournamentId={tournamentId} tournament={tournament}></Round>
{/each} 
{:else}
<p>something's bad !</p>
{/if}
</div>
