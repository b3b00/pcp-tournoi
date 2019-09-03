
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
    console.log(tournament);
    tournament = await tools.fetchTournament(id);
  });


  async function build() {
    console.log(tournament);
    console.log(tournamentId);
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

{#if (tournament.run.board == null || tournament.run.board == undefined)}
<input type="text" bind:value={startingRound}/>
<br>
<button on:click={build}>Build Me !</button>
{:else}
<p>to be started soon!</p>
{/if}