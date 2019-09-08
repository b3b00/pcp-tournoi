
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
  });


  async function build() {
    const uri = `/tournaments/${tournamentId}/board/$create?start=${startingRound}`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        });
        tournament = await res.json();
  }

  let startingRound = 8;

  

</script>

{#if (tournament !== undefined && 
  tournament.run !== undefined && 
  tournament.run.board == null || tournament.run.board == undefined)}
<input type="text" bind:value={startingRound}/>
<br>
<button on:click={build}>construire le tableau</button>
{:else}
{#each tournament.run.board.boards as board}
<li style="cursor:pointer" on:click={() => {moveMe("board", board.name,"board",board.id);}}>
{board.name}
</li>
{/each}
{/if}