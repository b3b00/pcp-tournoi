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

    export let team1;

    export let team2;

    let scores = [];



    function initScores() {
        let setCount = 1;
        if (tournament.options !== undefined && tournament.options.winningSets > 1) {
            setCount = 2 * tournament.options.winningSets - 1;
        }
        else {
            setCount = 1;
        }
        
        for (let i = 0; i < setCount; i++) {
            scores.push({ left: "0", right: "1" });
        }
        scores = scores;
    }

    

    onMount(async () => {
    });


    $:{
        tournament = tournament;
        if (tournament.options !== undefined && (scores == undefined || scores.length == 0)) {
            initScores();
        }
    }

</script>


    <table>
        <tr>
            <td></td>
                {#each scores as score,i (score)}
                <td align="center">{i+1}</td>
                {/each}
        </tr>
        <tr>
            <td class="teamName">{team1.name}</td>
            {#each scores as score (score)}
            <!---->
            <td><input type="number" min="0" placeholder="0" bind:value={score.left} /></td>
            {/each}
        </tr>
        <tr>
            <td class="teamName">{team2.name}</td>
            {#each scores as score (score)}
            <!-- bind:value={score.right}--> 
            <td><input type="number" min="0" placeholder="0" bind:value={score.right} /></td>
            {/each}
        </tr>
    </table>
