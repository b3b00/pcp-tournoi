<!-- display group preview : 
  - team names
  - score
-->
<!-- uses :
  - group_preview 
-->

<style>
  span.winner {
    color:green;
  }
  span.loser {
    color:red;
  }
  span.defaultStyle {
    color:rgb(26, 25, 25);
  }
</style>

<script>
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import Match from './match.svelte';

  const dispatch = createEventDispatcher();

  export let match;


  let leftStyle;

  let rightStyle;

  let defaultStyle;

  onMount(() => {
    defaultStyle = "defaultStyle";
    leftStyle = "w3-half "+defaultStyle;
    rightStyle = "w3-half "+defaultStyle;
    if (match.winner != null) {
      leftStyle = "w3-half" + (match.winner.id == match.leftTeam.id ? "winnerStyle" : "loserStyle");
      leftStyle = "w3-half" + (match.winner.id == match.rightTeam.id ? "winnerStyle" : "loserStyle");
    }
  })

  function openMatch(matchId) {
    let dial = document.getElementById(`match_${matchId}`);
    dial.showModal();
  }

</script>


<!-- {#if match !== undefined && match != null} -->
<div >
  <div on:click={() => {openMatch(match.id)}} class="w3-card w3-quarter  w3-container" style="clear:both">
    <div>
    <div class="w3-quarter {leftStyle}">
      {match.left.name}
    <!-- </div>
    <div class="w3-quarter {leftStyle}"> -->
       ({match.leftWonSet}) 
    </div>
    <div class="w3-quarter {leftStyle}">
        {match.right.name}
      <!-- </div>
      <div class="w3-quarter {leftStyle}"> -->
         ({match.rightWonSet}) *
      </div>
    </div>
  </div>
  
    
    <dialog id="match_{match.id}">
  <Match match={match}>
  </Match>  
    </dialog>

  
</div>
<!-- {/if}  -->