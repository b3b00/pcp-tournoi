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

  const dispatch = createEventDispatcher();

  export let match;


  let leftStyle;

  let rightStyle;

  onMount(() => {
    leftStyle = "w3-half "+defaultStyle;
    rightStyle = "w3-half "+defaultStyle;
    if (match.winner != null) {
      leftStyle = "w3-half" + (match.winner.id == match.leftTeam.id ? "winnerStyle" : "loserStyle");
      leftStyle = "w3-half" + (match.winner.id == match.rightTeam.id ? "winnerStyle" : "loserStyle");
    }
  })

  function openMatch() {
    alert("will open match later");
    // dispatch("open", { 'match': match })
  }

</script>

<div on:click={openMatch}>
  <div class="w3-third w3-row-padding w3-container">
    <div class="w3-half {leftStyle}">
      {match.left.name} ({match.leftWonSet})
    </div>
    <div class="w3-half {rightStyle}">
        {match.right.name} ({match.rightWonSet})
      </div>
  </div>
  <!--
    TODO : 
  <Match match={match}>
  </Match>
  -->
</div>