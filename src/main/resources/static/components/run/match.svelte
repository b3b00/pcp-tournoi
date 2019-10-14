<!-- display editable match -->
<!-- uses :
  - 0 
-->
<style type="text/scss">
@import "../../styles/global.scss";
    td.teamName {
        font-weight: bold;
    }
    tr {
        padding: 0 0 0 0; 
        border : 0 0 0 0;
    }
    td {
        width:20px;
        text-align: center;
        padding: 0 0 0 0; 
        border : 0 0 0 0;
    }
    input {
        width:50%;
    }
    table {
        padding: 0 0 0 0; 
        border : 0 0 0 0;
        width:500px;
    }
</style>
<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import {GroupDisplay} from '../groupDisplay.js';

    const dispatch = createEventDispatcher();

    export let tournament;

    export let match;

    let scores = [];

    onMount(async () => {
    });

    function save() {
        let dial = document.getElementById(`match_${match.id}`);
        dial.style.display = 'none';
        dispatch("close", { 'tournamentId': tournament.id })
    }

    function close() {
        let dial = document.getElementById(`match_${match.id}`);
        dial.style.display = 'none';
    }

    $:{
        tournament = tournament;        
    }

</script>
<div class="w3-modal-content">
    <header class="w3-container pcp-color1 w3-padding-16 w3-large w3-center">{@html GroupDisplay.getTeamDisplayName(match.left,match.leftTeamReferenceLabel)} &nbsp;-&nbsp;{@html GroupDisplay.getTeamDisplayName(match.right,match.rightTeamReferenceLabel)}
        <span on:click={() => {close()}} class="w3-large" style="float:right;cursor:pointer">&times;</span></header>
    <table>
        <tr>
            <td></td>
                {#if (match!== undefined && match.score !== undefined)}
                {#each match.score as score,i (score)}
                <td align="center">{i+1}</td>
                {/each}
                {/if}
        </tr>
        <tr>
            <td class="teamName">{@html GroupDisplay.getTeamDisplayName(match.left,match.leftTeamReferenceLabel)}</td>
            {#if (match!== undefined && match.score !== undefined)}
            {#each match.score as score (score)}
            <td><input type="text" class="w3-input" min="0" placeholder="0" bind:value={score.left} /></td>
            {/each}
            {/if}
        </tr>
        <tr>
            <td class="teamName">{@html GroupDisplay.getTeamDisplayName(match.right,match.rightTeamReferenceLabel)}</td>
            {#if (match!== undefined && match.score !== undefined)}
            {#each match.score as score (score)}
            <td><input type="text"  class="w3-input" min="0" placeholder="0" bind:value={score.right} /></td>
            {/each}
            {/if}
        </tr>
    </table>
    <button style="margin-left:32px" class="w3-button w3-section pcp-color1 w3-ripple" on:click={save}>OK</button>
</div>
