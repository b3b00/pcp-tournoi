<!-- preview group (ranked) in group phase view -->
<!-- clickable : opens group detail -->
<!-- uses :
  - 0
-->

<style>
  </style>
  
  <script>
    import {mover} from './nav.js';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
  
    import MatchPreview from './match_preview.svelte';

    const dispatch = createEventDispatcher();
  
    export let groupPlay;
  

  let moveMe;

  onMount(() => {
        moveMe = mover(dispatch);
    });
  
    function openGroup() {     
        moveMe("group", "poule "+groupPlay.group.name,"group",groupPlay.id);
    // }
    //   dispatch("open", { 'group': groupPlay })
    }

    function openMatch() {
      
    }
  
  </script>
  

  {#if groupPlay != null && groupPlay.rankings != null}
  <div class="w3-container" on:click={openGroup} >

    <ul class="w3-ul w3-border">
      <li><h4>group {groupPlay.group.name}</h4></li>
      {#each groupPlay.rankings as ranking} 
        <li>{ranking.team.name} - {ranking.points} </li>
      {/each}
    </ul>
  </div>
  {/if}