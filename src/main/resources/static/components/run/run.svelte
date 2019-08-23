<style>
    dialog {
        /* background-color: var(--bg); */
        color: var(--text-color);
        text-align: center;
        border: none;
        padding: 2rem;
        border-radius: 6px;
        box-shadow: 0 0 40px rgba(0, 0, 0, 0.1), 0 0 10px rgba(0, 0, 0, 0.25);
        max-width: 90vw;
    }
</style>

<script>

    import Match from './match.svelte';
    import BreadCrumb from './breadcrumb.svelte';
    import GroupPhase from './groups.svelte';
    import Group from './group.svelte';
    import { mover } from './nav.js';
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

    let group;

    let currentItem;

    onMount(async () => {
        // moveMe = mover(dispatch);
        currentBreadCrumb = [
            {
                "name": "run", "label": "run", "path": "run", "id": "1"
            }
        ];
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
            await fetchTournament(tournamentId);
        }
        tournament = tournament;

    });

    async function fetchTournament(id) {
        const res = await fetch(`/tournaments/${id}`);
        tournament = await res.json();
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
        console.log("==============")
        console.log("===  bread ===")
        console.log("==============")
        console.log(newBreadCrumb);
        console.log("==============")
        newBreadCrumb.forEach(item => {
            fetchTournament(tournamentId);
            if (item.name == "groupPhase") {

                groupPhase = tournament.run.groupPhase;
                {console.log("move to phase - 1")}
                {console.log(groupPhase)}
                {console.log("-----")}
            }
            if (item.name == "group") {       
                group = tournament.run.groupPhase.groups.filter(g => g.id == item.id)[0];
                {console.log("move to group - 1 ")}         
                {console.log(group)}
                {console.log("-----")}
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
        <Group groupPlay={group} tournament={tournament} on:move={moveScreen}></Group>
    {:else}
        <p>current state is {currentItem.name} - {currentItem.label}</p>
        <span style="cursor:pointer" on:click={() => {moveMe("groupPhase","poules","groupPhase",null)}}>poules</span>
    {/if}
{:else}
<span style="cursor:pointer" on:click={() => {moveMe("groupPhase","poules","groupPhase",null)}}>poules</span>
{/if}