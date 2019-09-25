<script>

  import { tools } from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';




  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    tournament = await tools.fetchTournament(tournamentId);
    build();
    // tseu tseu
  });


  async function build() {
    if (tournament !== undefined && tournament !== null && tournament.groups !== undefined && tournament.groups !== null) {
      if (tournament.run.board === undefined || tournament.run.board === null) {
        let t = (Math.log(tournament.groups.length) / Math.log(2)) % 1 === 0;
        // if (t) {
          const uri = `/tournaments/${tournamentId}/board/$create`; // ?start=${Math.sqrt(tournament.groups.length)}`;
          const res = await fetch(uri, {
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            method: "POST"
          });
          tournament = await res.json();
        // }
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
{#each tournament.run.board.boards as board}
<li style="cursor:pointer" on:click={() => {moveMe("board", board.name,"board",board.id);}}>
{board.name}
</li>
{/each}
<li style="cursor:pointer" on:click={() => {moveMe("newboard", "nouveau tableau","board",-1);}}>
  nouveau tableau
</li>
{/if}