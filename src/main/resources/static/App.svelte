<script>


	const STATE = {
		BUILD : "build",
		RUN : "run"
	}

	import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import { beforeUpdate, afterUpdate } from 'svelte'; 

 	import Build from './components/build/Build.svelte';
	import Run from './components/run/Run.svelte';


	let tournamentId = -1;

	let tournaments = [];

	let state = STATE.BUILD;

	onMount(async () => {
        fetchTournaments();
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
            fetchTournament(tournamentId);
        }
    });

	async function fetchTournaments() {
        const res = await fetch('/tournaments');
        tournaments = await res.json();        
    }

	function changeState(newState) {
		state = newState;
	}

	function setTournament(newTournamentId ) {
		tournamentId = newTournamentId;
	}

</script>

<div class="w3-panel w3-card startDialog">

		<p style="display:inline">
				<button on:click={() => changeState(STATE.BUILD)}>Configuration</button>
				<button on:click={() => changeState(STATE.RUN)}>Jeu</button>	 			
			</p>


<ul class="w3-ul w3-border">
<!-- <li>
<h2>Names</h2>
</li> -->
{#each tournaments as tournament}
	<li class="w3-bar" style="cursor: pointer;" on:click={() => {setTournament(tournament.id);}}> 
		{tournament.name}
	</li>
{/each}
</ul>
</div>


{#if (state == STATE.BUILD)}
	<Build tournamentId={tournamentId}></Build>
{:else}
	<Run tournamentId={tournamentId}></Run>
{/if}

