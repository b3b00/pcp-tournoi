<style type="text/scss">
@import "../../styles/global.scss";
  .clickable {
    cursor:pointer;
  }
  .column {
    width:25%;
    float:left;
    margin-right: 5%;
  }
  .item {
    padding-bottom: 5px;
    text-decoration: underline;
    text-align: center;
  }
</style>
<script>

  import { tools } from '../tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';




  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    build();
  });


  async function build() {
    if (tournament !== undefined && tournament !== null && tournament.groups !== undefined && tournament.groups !== null) {
      if (tournament.run.board === undefined || tournament.run.board === null) {
          const uri = `/tournaments/${tournamentId}/board/$create`; 
          const res = await fetch(uri, {
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            method: "POST"
          });
          tournament = await res.json();
      }
    }
  }

  let startingRound = 8;



</script>

{#if (tournament !== undefined && 
  tournament !== null &&
  tournament.run !== undefined && 
  tournament.run !== null && 
  tournament.run.board !== undefined && 
  tournament.run.board !== null && 
  tournament.run.board.boards !== undefined &&
  tournament.run.board.boards !== null
  )}
  
  <div  class="column">
      <div class="w3-card item" style="font-weight:bold;">En cours :</div>
      <div class="item" style="text-decoration: none;">&nbsp;</div>
{#each tournament.run.board.boards as board}
  {#if (!board.isDone)}
    <div class="clickable item "  on:click={() => {moveMe("board", board.name,"board",board.id);}}>
    {board.name}
    </div>
  {/if}
{/each}
  
<div class="clickable item " on:click={() => {moveMe("newboard", "nouveau tableau","board",-1);}}>
  <span  class="w3-button w3-large w3-circle w3-xlarge w3-ripple pcp-color1" title="nouveau tournoi" style="z-index:0;cursor:pointer" href="#">+</span>
</div>
</div>
<div class="column">
<div class="w3-card item" style="font-weight: bold">Finis : </div>
<div class="item" style="text-decoration: none;">&nbsp;</div>
  {#each tournament.run.board.boards as board}
    {#if (board.isDone)}
      <div class="clickable item "   on:click={() => {moveMe("board", board.name,"board",board.id);}}>
      {board.name}
      </div>
    {/if}
  {/each}
</div>
{/if}