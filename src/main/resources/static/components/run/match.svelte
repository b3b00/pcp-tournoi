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

    export let tournament;

    export let match;

    let scores = [];

    onMount(async () => {
    });


    $:{
        tournament = tournament;        
    }

</script>


    <table>
        <tr>
            <td></td>
                {#each match.score as score,i (score)}
                <td align="center">{i+1}</td>
                {/each}
        </tr>
        <tr>
            <td class="teamName">{match.leftTeam.name}</td>
            {#each match.score as score (score)}
            <td><input type="number" min="0" placeholder="0" bind:value={score.left} /></td>
            {/each}
        </tr>
        <tr>
            <td class="teamName">{match.rightTeam.name}</td>
            {#each match.score as score (score)}
            <td><input type="number" min="0" placeholder="0" bind:value={score.right} /></td>
            {/each}
        </tr>
    </table>
