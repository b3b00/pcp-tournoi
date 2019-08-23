<!-- preview group (ranked) in group phase view -->
<!-- clickable : opens group detail -->
<!-- uses :
  - 0
-->

<style>
  </style>
  
  <script>
    import {tools} from './tools.js';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
  
    import MatchPreview from './match_preview.svelte';

    const dispatch = createEventDispatcher();
  
    export let groupPlay;
  

  let moveMe;

  onMount(() => {
        moveMe = tools.mover(dispatch);
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

  <div class="w3-container w3-quarter" on:click={openGroup} >

        <div class="w3-padding w3-border"><h4>poule {groupPlay.group.name}</h4></div>
        {#each groupPlay.rankings as ranking} 
          <div class="w3-container w3-padding w3-border-right w3-border-left w3-border-bottom">
          <div class="w3-half" style="text-align: center">{ranking.team.name}</div>  
          <div class="w3-quarter">&nbsp;</div>
          <div class="w3-quarter" style="text-align: center">{ranking.points}</div>
          </div>
        {/each}
      
    </div>
  {/if}