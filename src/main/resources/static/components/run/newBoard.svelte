<style>
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

  let selectedTeamsCount = 0;

  onMount(async () => {
    moveMe = tools.mover(dispatch);
    await refresh();
  });

  async function refresh() {
    availableTeams = await tools.fetchAvailableTeams(tournamentId);
    
  }

  function toggleAll(selection) {
    availableTeams.forEach(t => {
      t.selected = selection;
    });
    availableTeams = availableTeams;
    selectedTeamsCount = availableTeams.filter(t => t.selected).length;
  }

  export let selectAll = true;

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
        selectedTeamsCount = availableTeams.filter(t => t.selected).length;
    }

    async function build() {
      let selectedTeams = availableTeams.filter(t => t.selected).map(t => t.id);
      let powerOfTwo = selectedTeams != undefined && selectedTeams !== null && (Math.log(selectedTeams.length) / Math.log(2)) % 1 === 0;
      if (powerOfTwo && boardName !== undefined && boardName !== null && boardName.length > 0) {
         tournament  = await tools.createBoardWithTeams(tournament.id,selectedTeams,boardName);
         refresh();
      }
      else {
        window.alert('Vous devez nommer le nouveau tableau\net sélectionner un nombre puissance de 2 d\'équipes.');
      }
      
    }
  

</script>

<div class="w3-container">
  
  {#if availableTeams !== undefined && availableTeams !== null && availableTeams.length > 0}
  <div>
  <p>{availableTeams.length} {availableTeams.length> 1 ? "équipe(s) disponible(s)": "équipe disponible"}
<span>
    {#if selectedTeamsCount > 0}
    - {selectedTeamsCount} {selectedTeamsCount > 1 ? "équipes sélectionnées": "équipe sélectionnée"}
    {/if}
</span>

  </p>
  

  <input type="text" placeholder="nom du tableau" bind:value={boardName}/>
  <button on:click={build}>construire le tableau</button> 
  
    <button on:click={() => {toggleAll(true);}}> Tout sélectionner</button>
  
  <button on:click={() => {toggleAll(false);}}> Tout désélectionner</button>
  
  </div>
  <div class="w3-card w3-third">     
    <ul class="w3-ul w3-border w3-card">
      {#each availableTeams as team}
          {#if (!teamtools.isTeamEmpty(team))}
              <li class="w3-display-container">        
                  <Team team={team} unTeamAble={false} selected={team.selected} on:selectionChanged={(data) => { selectTeam(team,data) }}/>       
              </li>
          {/if}
      {/each}
    </ul>  
    </div>



{:else}
<span>aucune équipe disponible</span>
  {/if}
  
</div>
