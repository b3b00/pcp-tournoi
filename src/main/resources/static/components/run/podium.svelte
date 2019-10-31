<style type="text/scss">
@import "../../styles/global.scss";
</style>
<script>

  import { tools } from '../tools.js';
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
 

</script>


{#if (board.isDone)}

<div class="w3-display-container">
  <div class="w3-cell w3-quarter">&nbsp;</div>
<div class="w3-cell w3-display-container w3-half ">
  <div class="w3-cell-row">
    <div class="w3-cell w3-third">&nbsp;</div>
    <div class="w3-cell w3-third w3-center"><span class='fas fa-medal' style='color:gold'>{board.ranking[0].name}</span></div>
    <div class="w3-cell w3-third">&nbsp;</div></div>
  <div class="w3-cell-row"></div>
    <div class="w3-cell w3-third w3-center"><span class='fas fa-medal' style='color:silver'>{board.ranking[1].name}</span></div>
    <div class="w3-cell w3-third w3-teal">&nbsp;</div>
    <div class="w3-cell w3-third w3-center"><span class='fas fa-medal' style='color:#CD7F32'>{board.ranking[2].name}</span></div>
  <div class="w3-cell-row"></div>
  <div class="w3-cell-row"></div>
  <div class="w3-cell w3-third w3-teal">&nbsp;</div>
    <div class="w3-cell w3-third w3-teal">&nbsp;</div>
    <div class="w3-cell w3-third w3-teal">&nbsp;</div>
  <div class="w3-cell-row"></div>
</div>
<div class="w3-cell w3-quarter">&nbsp;</div>
</div>
{/if}

