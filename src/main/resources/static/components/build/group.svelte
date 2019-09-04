<style>    
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import SelectableUL from './selectableUL.svelte';
    import {GroupDisplay} from '../groupDisplay.js';

    const dispatch = createEventDispatcher();

    let teams = [];

    export let tournamentId = -1;

    export let group = {};

    export let selected = false;

    const licenseeStar = "<span class='fa fa-star w3-display-center'></span>";

    
    function computeTeamNames() {
        group.teams.forEach(t => {
            let name = GroupDisplay.getTeamDisplayName(t);    
            t.name = name;
        })
    }

    onMount(async () => {
        computeTeamNames();
        group = group;
    });

    function unGroup(group, team) {
        dispatch("unGroup", { 'group':group,'team':team });
    }

    function selectGroup(state) {
        selected=state.detail.state;
        dispatch("selectionChanged",state);
    }

  
</script>

{#if (group.teams != null || group.teams.length > 0)}
<SelectableUL on:drop on:selectionChanged={selectGroup} payload={group} selected={selected} label={"Poule "+group.name}>
    {#each group.teams  as team}

   
    <li class="w3-display-container">
        {@html GroupDisplay.getTeamDisplayName(team,null)} <span on:click="{() => {unGroup(group,team)}}" class="w3-button w3-display-right">&times;</span>
    </li>
   
   
    {/each}
</SelectableUL>
{/if}
