<!-- display group preview : 
  - team names
  - score
-->
<!-- uses :
  - group_preview 
-->

<style>
  .match-margin {
    margin: 8px!important;
  }
  span.winner {
    color: lightgreen;
    font-weight: bold;
  }

  span.loser {
    color: red;
    font-style: italic;
  }

  span.defaultStyle {
    color: rgb(26, 25, 25);
  }
</style>

<script>
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import Match from './match.svelte';

  const dispatch = createEventDispatcher();

  export let match;

  export let tournament;


  let leftStyle;

  let rightStyle;

  let defaultStyle;

  function setStyle() {
    defaultStyle = "defaultStyle";
    leftStyle = defaultStyle;
    rightStyle = defaultStyle;
    if (match.winner != null) {
      leftStyle = (match.winner.id == match.left.id ? "winner" : "loser");
      rightStyle = (match.winner.id == match.right.id ? "winner" : "loser");
    }
  }


  onMount(() => {
    setStyle();
  })

  function openMatch(matchId) {
    let dial = document.getElementById(`match_${matchId}`);
    dial.showModal();
  }

  async function saveMatch(savedMatch) {

    const res = await fetch(`/tournament/${tournament.id}/match`,
      {
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        method: 'PUT',
        body: JSON.stringify(savedMatch)
      });
    match = await res.json();
    dispatch('matchSaved',{});
    setStyle();    
  }

</script>


<div>
  <div on:click={()=> {openMatch(match.id)}} class="w3-card w3-quarter w3-container match-margin w3-padding" style="clear:both">
    <div>
      <div class="w3-quarter">
        <span class=" {leftStyle}">
          {match.left.name}
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {leftStyle}">
          ({match.leftWonSet})
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {rightStyle}">
          {match.right.name}
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {rightStyle}">
          ({match.rightWonSet})
        </span>
      </div>
    </div>
  </div>


  <dialog id="match_{match.id}" on:close={()=> {saveMatch(match)}}>
    <Match match={match} tournament={tournament}>
    </Match>
  </dialog>


</div>