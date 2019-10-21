<style type="text/scss">
@import "../../styles/global.scss";
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

  let displayedRounds;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    computedisplayedRounds();
  });

  async function refresh(data) {
    let boardId = board.id;
    let newBoard = await tools.fetchBoard(tournamentId, boardId);
    board = newBoard;
    computedisplayedRounds();
  }

  function computedisplayedRounds() {
    console.log("computing displayed rounds");
    const notDone = board.rounds.filter(r => !r.isDone);
    console.log()
    displayedRounds = notDone.slice(0, 3);
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
  <div class="w3-quarter">
<span class='fa fa-refresh w3-display-center refresh' on:click={refresh}>&nbsp;</span>
</div>
<div class="w3-three-quarter w3-xlarge">
{board.name}
</div>
</div>

{#if (board.isDone)}
<ol>
  {#each getRanking() as r}
  <li>{r.name}</li>
  {/each}
  </ol>
{/if}
<div class="w3-container">
{#if displayedRounds !== null && displayedRounds!== undefined}
{#each displayedRounds as round}
<Round round={round} roundId={round.id} tournamentId={tournamentId} tournament={tournament} on:refresh={refresh}></Round>
{/each} 
{:else}
<p>something's bad !</p>
{/if}
</div>
