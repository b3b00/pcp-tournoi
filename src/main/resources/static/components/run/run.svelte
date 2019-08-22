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
    import GroupPhase from './groups.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    export let tournamentId;

    let tournament = {};

    let groupPhase;

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
        if (tournament.run == null || tournament.run === undefined) {
            const res = await fetch(`/tournaments/${id}/groupPhase/$create`,
            {
            headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: 'POST'});
            tournament = await res.json();
            groupPhase = tournament.run.groupPhase;
        }
        else {
            groupPhase =tournament.run.groupPhase;
        }
    }

</script>

<!-- //TODO -->
<GroupPhase phase={groupPhase}></GroupPhase>



