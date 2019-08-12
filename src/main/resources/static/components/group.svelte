<style>    
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import SelectableUL from './selectableUL.svelte';

    const dispatch = createEventDispatcher();

    let teams = [];

    export let tournamentId = -1;

    export let group = {};

    export let selected = false;

    
    function computeTeamNames() {
        group.teams.forEach(t => {
            t.name = t.player1.name + " - "+t.player2.name;
        })
    }

    onMount(async () => {
        computeTeamNames();
        group = group;
    });

    function unTeam(group, team) {
        //dispatch("unteam", { 'team':team,'player':player });
    }

    function selectGroup(state) {
        selected=state.detail.state;
        dispatch("selectionChanged",state);
    }

     function teamName(team) {
         return `${team.player1} - ${team.player2}`;
     }
</script>

{#if (group.teams != null || group.teams.length > 0)}
<SelectableUL on:selectionChanged={selectGroup} selected={selected} >
    {#each group.teams  as team}

   
    <li class="w3-display-container">
        {team.name} 
        <!-- <span on:click="{() => {unTeam(team,team.player1<span class={team.name ? "fa fa-star w3-display-center" : ""}></span>)}}" class="w3-button w3-display-right">&times;</span>         -->
    </li>
   
   
    {/each}
</SelectableUL>
{/if}
