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

    const licenseeStar = "<span class='fa fa-star w3-display-center'></span>";

    
    function computeTeamNames() {
        group.teams.forEach(t => {
            let name = "";
            if (t.player1 != null)  {
                if (t.player2 != null) {                    
                    let name1 = t.player1.isLicensed ? t.player1.name+"    "+licenseeStar : t.player1.name;
                    let name2 = t.player2.isLicensed ? t.player2.name+"    "+licenseeStar : t.player2.name;
                    name = name1 + " - "+name2;
                }
                else {
                    name = t.player1.name;
    const licenseeStar = "<span class='fa fa-star w3-display-center'></span>";
                    name = t.player1.isLicensed ? name + licenseeStar : name;
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
        {@html team.name} <span on:click="{() => {unGroup(group,team)}}" class="w3-button w3-display-right">&times;</span>
    </li>
   
   
    {/each}
</SelectableUL>
{/if}
