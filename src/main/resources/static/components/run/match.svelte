<style>
    td.teamName {
        font-weight: bold;
    }
</style>
<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    export let tournamentId;

    export let tournament;

    export let team1;

    export let team2;

    export let team1Id;

    export let team2Id;

    export let setCount = 1;

    let scores = [];

    function initScores() {
        if (tournament.options !== undefined && tournament.options.winningSets > 1) {
            setCount = 2 * tournament.options.winningSets - 1;
        }
        else {
            setCount = 1;
        }
        console.log(`initialize scores for a ${setCount} sets` );
        for (let i = 0; i < setCount; i++) {
            scores.push({ left: "0", right: "1" });
        }
        scores = scores;
    }

    

    onMount(async () => {
        console.log("tournamentId : " + tournamentId);
        console.log(team1);
        console.log(team2);
        console.log(tournament);
        //initScores();
    });


    $:{
        tournament = tournament;
        if (tournament.options !== undefined && (scores == undefined || scores.length == 0)) {
            initScores();
        }
    }

</script>
<div class="w3-container">

    <table>
        <tr>
            <td></td>
                {#each scores as score,i (score)}
                <td align="center">{i}</td>
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
</div>