<style type="text/scss">
    @import "../../styles/global.scss";

    .selected {
        @extend .pcp-color1;
    }

    .notselected {
        background-color: white;
    }

</style>
<script>

    import Group from './group.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import {GroupDisplay} from '../groupDisplay.js';
    import {teamtools, modes} from './teams.js';


    function dragstart (team) {
        return function(ev) {
            let content = JSON.stringify(team);
            ev.dataTransfer.setData("application/json", content);
        }
    }
    
    function drop (ev) {
            ev.preventDefault();
            let tjson = ev.detail.event.dataTransfer.getData('application/json');                        
            let team = JSON.parse(tjson);                        
            let realGroup = ev.detail.payload;
            if (realGroup != null) {                
                addTeams([team],realGroup);
                computeUngroupedTeams();
            }
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
            if (tournament.options.mode == "SINGLE" && (tournament.teams === undefined || tournament.teams === null || tournament.teams.length == 0 || tournament.teams.length != tournament.players.length)) {
                tournament = await teamtools.computeTeams(modes.SINGLE, tournament.id);
                computeUngroupedTeams();
            }      

        }  
        catch(error) {       
            console.log(error);
        }
    }
    
  

    function selectGroup(group, data) {
        group.selected = data.detail.state;
        tournament.groups.forEach(g => {
            if (g.id == group.id) {
                g.selected = data.detail.detail.state;
            }
            else {
                if (g.selected === undefined) {
                    g.selected = false;
                }
            }
        })
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
        const uri = `/tournaments/${tournamentId}/groups?number=${groupNumber}`;        
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
    

    function onUnGroup(data) {
        let group = data.detail.group;
        let team = data.detail.team;
        unGroup(group, team);
    }

    async function unGroup(group, team) {
        if (group != null && team != null) {
            tournament.groups.forEach(async g => {
                if (g != null) {
                    let found = false;
                    let teamIn = g.teams.filter(t => t.id == team.id);

                    if (teamIn != null && teamIn.length > 0) {
                        let teams = g.teams.filter(t => t.id != team.id);
                        g.teams = teams;
                        const uri = `/tournaments/${tournamentId}/groups`;        
                        const res = await fetch(uri, {
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            method: "PUT",
                            body:JSON.stringify(g)
                        });
                        tournament = await res.json();
                        computeUngroupedTeams(); 
                    }
                }
            });
            tournament.teams = tournament.teams;
            computeUngroupedTeams();
        } 
    }

    function selectUngroupedTeam(team) {
        let realTeam = ungroupedTeams.find(t => t.id == team.id);
        realTeam.selected = !realTeam.selected;
        ungroupedTeams = ungroupedTeams;
    }

    async function buildGroup() {
        let selectedTeams = ungroupedTeams.filter(t => t.selected);
        let selectedGroups = tournament.groups.filter(g => g.selected);
        if (selectedGroups.length == 1) {
            let group = selectedGroups[0];
            if (selectedTeams.length > 0) {
                addTeams(selectedTeams,group);
            }
            else {
                alert('Vous devez sélectionner au moins 1 équipe.')
            }
        }
        else {
            alert('Vous devez sélectionner une et une seule poule')
        }
    }

    async function addTeams(teams, group) {
        teams.forEach(t => group.teams.push(t));
        const uri = `/tournaments/${tournamentId}/groups`;        
        const res = await fetch(uri, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "PUT",
            body:JSON.stringify(group)
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
                <Group on:drop={drop} group={group} on:unGroup={onUnGroup} on:selectionChanged={(data) => { selectGroup(group,data) }}/>       
                <!-- <Group group={group} on:unteam={onUnTeam} selected={team.selected} on:selectionChanged={(data) => { selectGroup(group,data) }}/>        -->
            </li>
    {/each}
    </ul>
{:else}
    <p>no groups</p>
{/if}
</div>

<!-- action sur les équipes et joueurs -->
<div class="w3-container w3-cell" style="width:10%">
   
    <button on:click={buildGroup} class="awesome fa fa-arrow-left">        
    </button>

</div>

<!-- équipes non affectées -->

<div class="w3-container w3-cell" style="width:60%">
    <ul class="w3-ul w3-border w3-card">
        
        {#each ungroupedTeams as team (team.id)}
        <li draggable=true on:dragstart={dragstart(team)}  on:click={() => {selectUngroupedTeam(team)}} class={team.selected ? "selected" : "notselected"}>            
            <span>{@html GroupDisplay.getTeamDisplayName(team,null)}<span>
        </li>
        {/each}       
    </ul>
</div>
