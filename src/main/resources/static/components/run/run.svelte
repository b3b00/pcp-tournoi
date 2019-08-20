<style>
  dialog {
  /* background-color: var(--bg); */
  color: var(--text-color);
  text-align: center;
  border: none;
  padding: 2rem;
  border-radius: 6px;
  box-shadow: 0 0 40px rgba(0,0,0,0.1), 0 0 10px rgba(0,0,0,0.25);
  max-width: 90vw;
}



</style>

<script>

    import Match from './match.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    export let tournamentId;

    let tournament = {};
    let team1 = {};
    let team2 = {};
    let setCount = 1;

    onMount(async () => {
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
            await fetchTournament(tournamentId);            
        }
        tournament = tournament;

    });

    async function fetchTournament(id) {
        const res = await fetch(`/tournaments/${id}`);
        tournament = await res.json();
        tournamentId = tournament.id;
        team1 = tournament.teams[0];
        team2 = tournament.teams[1];
    }

    let startIndex = 0;

    function showDial() {
        var dialog = document.querySelector('dialog');
        if (startIndex < tournament.teams.length-1) {
            team1 = tournament.teams[startIndex];
            team2 = tournament.teams[startIndex+1];
            dialog.showModal();
        }
    }

    function hideDial() {
        var dialog = document.querySelector('dialog');
        dialog.close();
    }

</script>
<p> coming ...</p>
<input type="number" placeholder="0" bind:value={startIndex}/>
<button on:click={showDial}>show</button>

<dialog >

    
    <Match tournament={tournament} team1={team1} team2={team2} >
    </Match>


    <button on:click={hideDial}>Close</button>
    

</dialog>    

