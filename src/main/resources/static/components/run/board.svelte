<style type="text/scss">
.refresh {
  cursor:pointer;
  font-size: x-large;
  padding-left: 16px;
  padding-bottom: 16px;
}
</style>
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

  async function refresh(data) {
    let boardId = board.id;
    let newBoard = await tools.fetchBoard(tournamentId, boardId);
    board = newBoard;
  }

  function getRanking() {
    let last = board.rounds[board.rounds.length-1];
    if (last.matches.length == 2) {
      let ranking = [null,null,null,null];
      let f = last.matches.filter(m => m.finale)[0];
      ranking[0] = f.winner;
      ranking[1] = f.left.id == f.winner.id ? f.right : f.left;
      let sf = last.matches.filter(m => m.smallFinale)[0];
      ranking[2] = sf.winner;
      ranking[3] = sf.left.id == sf.winner.id ? sf.right : sf.left;
      return ranking;
    }
    return [];
  }
  

</script>
<div class="w3-container" style="clear:both">
<span style='' class='fa fa-refresh w3-display-center refresh' on:click={refresh}>&nbsp;</span>
</div>
{#if (board.isDone)}
<ol>
  {#each getRanking() as r}
  <li>{r.name}</li>
  {/each}
  </ol>
{/if}
<div class="w3-container">
{#if board !== null && board!== undefined}
{#each board.rounds as round}
<Round round={round} roundId={round.id} tournamentId={tournamentId} tournament={tournament} on:refresh={refresh}></Round>
{/each} 
{:else}
<p>something's bad !</p>
{/if}
</div>
