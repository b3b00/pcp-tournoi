<style>
.refresh {
  cursor:pointer;
  font-size: x-large;
  padding-left: 16px;
  padding-bottom: 16px;
}
</style>
<script>

  import { tools } from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';
  import Team from '../build/team.svelte';
  import {teamtools, modes} from '../build/teams.js';


  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  let availableTeams = [];

  let boardName = "";

  let moveMe;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    refresh();
  });

  async function refresh(data) {
    availableTeams = tools.fetchAvailableTeams(tournamentId);
    
  }

  function selectTeam(team, data) {
        team.selected = data.detail.state;
        availableTeams.forEach(t => {
            if (t.id == team.id) {
                t.selected = data.detail.detail.state;
            }
            else {
                if (t.selected === undefined) {
                    t.selected = false;
                }
            }
        });
    }

    function build() {
      let selectedTeams = availableTeams.filter(t => t.selected);
      let powerOfTwo = selectedTeams != undefined && selectedTeams !== null && (Math.log(selectedTeams.length) / Math.log(2)) % 1 === 0;
      if (powerOfTwo && boardName !== undefined && boardName !== null && boardName.length > 0) {
        
      }
      else {
        window.alert('Vous devez nommer le nouveau tableau\net sélectionner un nombre puissance de 2 d\'équipes.');
      }
      
    }
  

</script>

<div>
  

  {#if availableTeams !== undefined && availableTeams !== null && availableTeams.length > 0}

    <input type="text" bind:value={boardName}/>
    <ul class="w3-ul w3-border w3-card">
      {#each availableTeams as team}
          {#if (!teamtools.isTeamEmpty(team))}
              <li class="w3-display-container">        
                  <Team team={team} selected={team.selected} on:selectionChanged={(data) => { selectTeam(team,data) }}/>       
              </li>
          {/if}
      {/each}
    </ul>  
    <button on:click={build}>construire le tableau</button> 
{:else}
<span>pas d'équipe disponible</span>
  {/if}
  
</div>
