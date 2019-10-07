<!-- preview group (ranked) in group phase view -->
<!-- clickable : opens group detail -->
<!-- uses :
  - 0
-->

<style type="text/scss">
  </style>
  
  <script>
    import {tools} from './tools.js';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import {GroupDisplay} from '../groupDisplay.js';
  
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

    function getIsDoneflag(groupPlay) {
      let done =  "<span style='color:green' class='fa fa-check w3-display-center'></span>";
      return groupPlay.isDone ? done : "";
    }
  
  </script>
  

  {#if groupPlay != null && groupPlay.rankings != null}

  <div class="w3-container w3-quarter" on:click={openGroup} >

        <div class="w3-padding w3-border"><h4>poule {groupPlay.group.name} {@html getIsDoneflag(groupPlay)}</h4></div>
        {#each groupPlay.rankings as ranking} 
          <div class="w3-container w3-padding w3-border-right w3-border-left w3-border-bottom">
          <div class="w3-half" style="text-align: center">{@html GroupDisplay.getTeamDisplayName(ranking.team,null)}</div>  
          <div class="w3-quarter">&nbsp;</div>
          <div class="w3-quarter" style="text-align: center">{ranking.points}</div>
          </div>
        {/each}
      
    </div>
  {/if}