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

    let ungroupedTeams = []

    onMount(async () => {
        tournament = {
            id: tournamentId,
            teams: [],
            groups: [],
            players: []
        };

        if (tournamentId != -1) {
            await load(); 
        }

    });


    async function load() {
        try {
        const res = await fetch(`/tournaments/${tournamentId}`);
        tournament = await res.json(); 
        computeUngroupedTeams();
        }        
        catch(error) {       
            console.log(error);
        }
    }
    
  

    function selectGroup(group, data) {
    }

    async function computeUngroupedTeams() {
        ungroupedTeams = [];
        let grouped = [];
        if (tournament.groups !== undefined && tournament.groups != null && tournament.groups.length > 0) {
        tournament.groups.forEach(group => {
            group.teams.forEach(t => {
                grouped.push(t);
            })
        });
        }        
        ungroupedTeams = substract(tournament.teams, grouped);
    }

    function substract(a, b) {    
        let res = a.filter(aa => !b.find(bb => bb.id == aa.id));
        return res;
    }


    async function createGroups() {
        const uri = `/tournaments/${tournamentId}/groups/${groupNumber}`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST"
        });
        tournament = await res.json();
        computeUngroupedTeams(); 
    }

    async function deleteGroups() {
        const uri = `/tournaments/${tournamentId}/groups`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "DELETE"
        });
        tournament = await res.json();
        computeUngroupedTeams(); 
    }
    

</script>

<div class="w3-panel w3-card startDialog" >




    <label for="name">Nombre de poules : </label>
    <input type="text" name = "number" id="number" class="w3-input" bind:value={groupNumber}/>
    <button on:click={createGroups}>Générer les poules</button>
    <button on:click={deleteGroups}>Supprimer toutes les poules</button>
    

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
<!-- équipes non affectées -->

<div class="w3-container w3-cell" style="width:60%">
    <ul class="w3-ul w3-border w3-card">
        
        {#each ungroupedTeams as team}
        <li  on:click={() => {}}> <!-- style={player.selected ? "background-color:lightgray;" : "background-color:white"}>            -->
            <span>{team.name}<span>
        </li>
        {/each}       
    </ul>
</div>
