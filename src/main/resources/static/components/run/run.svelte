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

</script>
<p> coming ...</p>
<Match tournament={tournament} team1={team1} team2={team2} >
    
</Match>

