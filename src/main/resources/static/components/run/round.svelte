
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

  function refresh() {

  }

</script>

<p>ID : {round.id}</p>
<p>MATCHES : {round.matches.length}</p>

<!-- <div class="w3-container">
    {#each round.matches as match }
    <MatchPreview match={match} tournament={tournament} on:move on:matchSaved={refresh}/>
  {/each}
  

</div> -->
