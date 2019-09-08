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
  import {GroupDisplay} from '../groupDisplay.js';

  import MatchPreview from './match_preview.svelte';

  const dispatch = createEventDispatcher();

  export let tournament;

  export let groupPlay;

  let ranks;

  let moveMe;

  onMount(() => {
        moveMe = tools.mover(dispatch);
        if (groupPlay !== undefined) {
          ranks = groupPlay.rankings;
        }
  });

  

  async function refresh() {
    let groupId = groupPlay.id;
    let newGroupPlay = await tools.fetchGroupPlay(groupId);
    groupPlay = newGroupPlay;
    ranks = groupPlay.rankings;
  }

</script>

<!-- TODO titre du groupe ? -->
<div class="w3-half">
  {#each groupPlay.matches as match }
    <MatchPreview match={match} tournament={tournament} on:move on:matchSaved={refresh}/>
  {/each}
</div>
<div class="w3-half">
  <ul class="w3-ul w3-border w3-quarter">
      <li><h4>classement:</h4></li>
      {#if (ranks != null && ranks !== undefined)}  
        {#each ranks as ranking (ranking.team.id)} 
          <li>{@html GroupDisplay.getTeamDisplayName(ranking.team,null)} - {ranking.points} </li>
        {/each}
      {/if}     
    </ul>
</div>
