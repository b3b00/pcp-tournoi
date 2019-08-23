<!-- view group matches and ranking -->
<!-- matches and clickable for edit -->
<!-- uses :
  - match_preview
    - match ?
  - ranking
-->

<!-- TODO -->

<script>

  import {mover} from './nav.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import MatchPreview from './match_preview.svelte';

  const dispatch = createEventDispatcher();

  export let tournament;

  let moveMe;

  onMount(() => {
        moveMe = mover(dispatch);
  });

  export let groupPlay;


</script>

<!-- TODO titre du groupe ? -->
  {#each groupPlay.matches as match,i}
    <MatchPreview match={match} tournament={tournament} on:move/>
  {/each}
