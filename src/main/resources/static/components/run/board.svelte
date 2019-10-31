<style type="text/scss">
@import "../../styles/global.scss";
</style>
<script>

  import { tools } from '../tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';
  import Round from './round.svelte';
  import Podium from './podium.svelte';



  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  export let board;

  export let boardId;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
  });

  async function refresh(data) {
    let boardId = board.id;
    let newBoard = await tools.fetchBoard(tournamentId, boardId);
    board = newBoard;
  }
 

</script>

<div class="w3-container" style="clear:both">
  <div class="w3-quarter">
<span class='fa fa-refresh w3-display-center refresh' on:click={refresh}>&nbsp;</span>
</div>
<div class="w3-three-quarter w3-xlarge">
{board.name}
</div>
</div>

<Podium board={board} boardId={boardId} tournament={tournament} tournamentId={tournamentId}/>

<div class="w3-container">
{#if board !== null && board!== undefined}
{#each board.rounds as round}
<Round round={round} roundId={round.id} tournamentId={tournamentId} tournament={tournament} on:refresh={refresh}></Round>
{/each} 
{:else}
<p>something's bad !</p>
{/if}
</div>
