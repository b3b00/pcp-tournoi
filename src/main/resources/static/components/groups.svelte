<script>

    import Group from './group.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const modes = {
        RANDOM: "RANDOM",
        MIX: "MIX"
    }

    const dispatch = createEventDispatcher();

    export let tournamentId = -1;

    let tournament = {};

    let groupNumber = 8;

    onMount(async () => {
        tournament = {
            id: tournamentId,
            teams: [],
            players: []
        };

        if (tournamentId != -1) {
            await load(); 
            //stub();
        }

    });


    async function load() {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json();        
    }
    
    // function p(i) {
    //     return {
    //         name: "palyer "+i,
    //         id: i,
    //         isLicensed:(i%2 == 0)
    //     };
    // }

    // function t(i) {
    //     let player1 = p(i);
    //     let player2 = p(i+1);
    //     return {
    //         id:i,
    //         player1 : player1,
    //         player2 : player2,
    //         name:`${player1.name} - ${player2.name}` 
    //     }
    // }

    // function g(i) {
    //     return {
    //         id:i,
    //         name:"group "+i,
    //         teams:[t(i),t(i+1)],            
    //     }
    // }

    //  function stub() {
    //      tournament.groups = [g(1),g(5)];   
    //      console.log(tournament.groups);         
    //  }

     function selectGroup(group, data) {
     }

    

</script>

<div class="w3-panel w3-card startDialog" >




    <label for="name">Nombre de poules : </label>
    <input type="text" name = "number" id="number" class="w3-input" bind:value={groupNumber}/>

</div>

<div class="w3-container w3-cell" style="width:60%">
{#if (tournamentId != -1 && tournament.groups !==  undefined && tournament.groups !== null && tournament.groups.length > 0)} 
    <ul class="w3-ul w3-border w3-card">
    {#each tournament.groups as group}
            <li class="w3-display-container">        
                <Group group={group}  on:selectionChanged={(data) => { selectGroup(group,data) }}/>       
                <!-- <Group group={group} on:unteam={onUnTeam} selected={team.selected} on:selectionChanged={(data) => { selectGroup(group,data) }}/>        -->
            </li>
    {/each}
    </ul>
{:else}
    <p>no groups</p>
{/if}
</div>
