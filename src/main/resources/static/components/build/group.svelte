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
            let name = "";
            if (t.player1 != null)  {
                if (t.player2 != null) {
                    name = t.player1.name + " - "+t.player2.name;
                }
                else {
                    name = t.player1.name;
                }
            }
            else if (t.player2 != null) {
                name = t.player2.name;
            }
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
        {team.name} <span on:click="{() => {unGroup(group,team)}}" class="w3-button w3-display-right">&times;</span>
    </li>
   
   
    {/each}
</SelectableUL>
{/if}
