<!-- display editable match -->
<!-- uses :
  - 0 
-->
<style>
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

    export let tournament;

    export let match;

    let scores = [];

    onMount(async () => {
    });

    function save() {
        let dial = document.getElementById(`match_${match.id}`);
        dial.close();
    }

    $:{
        tournament = tournament;        
    }

</script>


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
    <button class="w3-button w3-section w3-teal w3-ripple" on:click={save}>close</button>
