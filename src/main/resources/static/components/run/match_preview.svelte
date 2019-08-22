<!-- display group preview : 
  - team names
  - score
-->
<!-- uses :
  - group_preview 
-->

<style>
  span.winner {
    color:red;
  }
  span.loser {
    color:black;
  }
  span.defaultStyle {
    color:black;
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
    dispatch("setTournament", { 'match': match })
  }

</script>

<div on:click="openMatch">
  <div class="w3-row-padding">
    <div class="w3-half {leftStyle}">
      {match.leftTeam.name} (leftWonSet) - {match.rightTeam.name} (rightWonSet)
    </div>
    <div class="w3-half {rightStyle}">
      </div>
  </div>
</div>