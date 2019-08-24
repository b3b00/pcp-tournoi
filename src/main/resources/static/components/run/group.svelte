<!-- view group matches and ranking -->
<!-- matches and clickable for edit -->
<!-- uses :
  - match_preview
    - match ?
  - ranking
-->

<!-- TODO -->

<script>

  import {tools} from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import MatchPreview from './match_preview.svelte';

  const dispatch = createEventDispatcher();

  export let tournament;

  export let groupPlay;

  let moveMe;

  onMount(() => {
        moveMe = tools.mover(dispatch);
  });

  

  async function refresh() {
    // TODO : fetch tournament
    let groupId = groupPlay.id;
    let newtournament = await tools.fetchTournament(tournament.id);
    let newgroupPlay = tournament.run.groupPhase.groups.filter(g => g.id == groupId)[0];
    tournament = newtournament;
    groupPlay = newgroupPlay;
  }

</script>

<!-- TODO titre du groupe ? -->
<div class="w3-half">
  {#each groupPlay.matches as match,i}
    <MatchPreview match={match} tournament={tournament} on:move on:matchSaved={refresh}/>
  {/each}
</div>
<div class="w3-half">
  <ul class="w3-ul w3-border w3-quarter">
      <li><h4>classement:</h4></li>
      {#each groupPlay.rankings as ranking (ranking.team.id)} 
        <li>{ranking.team.name} - {ranking.points} </li>
      {/each}      
    </ul>
</div>
