<!-- display group preview : 
  - team names
  - score
-->
<!-- uses :
  - group_preview 
-->

<style type="text/scss">
  .match-margin {
    margin: 8px !important;
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
  import { GroupDisplay } from '../groupDisplay.js';

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


  function getStyle(team) {
    if (team !== undefined && match !== undefined) {
      if (match.winner !== undefined && match.winner !== null) {
        return (match.winner.id == team.id ? "winner" : "loser");
      }
    }
    return "";
  }

  onMount(() => {
    setStyle();
  })

  function openMatch(matchId) {
    if (match.left !== null && match.right !== null && match.left !== undefined && match.right !== undefined) {
      let dial = document.getElementById(`match_${matchId}`);
      dial.style.display = 'block';
    }
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
    dispatch('matchSaved', {});
    setStyle();
  }

</script>

{#if (match !== null && match !== undefined)}
{#if match.finale}
<p>finale</p> 
{/if}
{#if match.smallFinale}
<p>petite finale</p> 
{/if}
<div>
  <div on:click={()=> {openMatch(match.id)}} class="w3-card  w3-container match-margin w3-padding" style="clear:both">
    <div>
      <div class="w3-quarter">
        <span class=" {getStyle(match.left)}">
            {@html GroupDisplay.getTeamDisplayName(match.left, match.leftTeamReferenceLabel)}          
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {getStyle(match.left)}">
          ({match.leftWonSet})
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {getStyle(match.right)}">
            {@html GroupDisplay.getTeamDisplayName(match.right, match.rightTeamReferenceLabel)}
        </span>
      </div>
      <div class="w3-quarter">
        <span class=" {getStyle(match.right)}">
          ({match.rightWonSet})
        </span>
      </div>
    </div>
  </div>

<div class="w3-modal w3-animate-zoom" id="match_{match.id}">
    <Match match={match} tournament={tournament} on:close={() => {saveMatch(match)}}>
    </Match>
</div>


</div>
{:else}
something bad
{/if}