<script>

    import Match from './match.svelte';
    import BreadCrumb from './breadcrumb.svelte';
    import GroupPhase from './groups.svelte';
    import Group from './group.svelte';
    import Board from './board.svelte';
    import { tools } from './tools.js';
    import { onMount } from "svelte";
    import { createEventDispatcher } from "svelte";

    const dispatch = createEventDispatcher();

    function moveMe(name, label, path, id) {
        //const dispatch = createEventDispatcher();
        let s = {
            "name": name,
            "label": label,
            "path": path,
            "id": id
        };
        currentBreadCrumb = [...currentBreadCrumb, s];
        currentItem = currentBreadCrumb[currentBreadCrumb.length - 1];
    }

    export let tournamentId;

    let tournament = {};

    let groupPhase;

    let board;

    let group;

    let currentItem;

    onMount(async () => {        
        currentBreadCrumb = [
            {
                "name": "tournoi", "label": "tournoi", "path": "tournoi", "id": "1"
            }
        ];
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
            await fetchTournament(tournamentId);
        }
        tournament = tournament;

    });

    async function fetchTournament(id) {        
        tournament = await tools.fetchTournament(id);
        tournamentId = tournament.id;
        if (tournament.run == null || tournament.run === undefined) {
            const res = await fetch(`/tournaments/${id}/groupPhase/$create`,
                {
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    method: 'POST'
                });
            tournament = await res.json();
            groupPhase = tournament.run.groupPhase;
        }
        else {
            groupPhase = tournament.run.groupPhase;
        }
        
    }

    let currentBreadCrumb;

    function moveScreen(data) {
        currentBreadCrumb = [...currentBreadCrumb, data.detail];
        currentItem = currentBreadCrumb[currentBreadCrumb.length - 1];
        navigator(null);
    }

    

    function navigator(data) {
        let newBreadCrumb = currentBreadCrumb;
        if (data != null) {
            newBreadCrumb = data.detail.items;        
        }
        newBreadCrumb.forEach(item => {
            fetchTournament(tournamentId);
            if (item.name == "groupPhase") {
                groupPhase = tournament.run.groupPhase;
            }
            if (item.name == "group") {       
                group = tournament.run.groupPhase.groups.filter(g => g.id == item.id)[0];
            }
        });
        currentItem = newBreadCrumb[newBreadCrumb.length - 1];
        currentBreadCrumb = newBreadCrumb;
    }

</script>

<BreadCrumb items={currentBreadCrumb} on:click={navigator}></BreadCrumb>
<hr>


{#if currentItem != null && currentItem !== undefined}
    {#if (currentItem.name == "groupPhase")}
        <GroupPhase phase={groupPhase} on:move={moveScreen}></GroupPhase>
    {:else if (currentItem.name == "group")}
        <Group groupPlay={group} tournament={tournament} on:move={moveScreen} ></Group>
    {:else if (currentItem.name == "board")}        
       <Board tournament={tournament} tournamentId={tournament.id} on:move={moveScreen}></Board> 
    {:else}
    <ul>
    <li style="cursor:pointer" on:click={() => {moveMe("groupPhase","poules","groupPhase",null)}}>poules</li>

    <li style="cursor:pointer" on:click={() => {moveMe("board","tableaux","board",null)}}>board</li>
    </ul>
    {/if}
{:else}
<ul>

<li style="cursor:pointer" on:click={() => {moveMe("groupPhase","poules","groupPhase",null)}}>poules</li>

<li style="cursor:pointer" on:click={() => {moveMe("board","tableaux","board",null)}}>board</li>

</ul>
{/if}