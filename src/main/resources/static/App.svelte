<style>
	li.selected {
		background-color: lightgray;
	}
	li.selectable {
		cursor: pointer;
	}
</style>
<script>
	const notSelectedStyle = "w3-bar selectable";
	const selectedStyle = notSelectedStyle+" selected";

	const STATE = {
		BUILD : "build",
		RUN : "run",
		HOME : "home"
	}

	import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import { beforeUpdate, afterUpdate } from 'svelte'; 

 	import Build from './components/build/Build.svelte';
	import Run from './components/run/run.svelte';   


	let tournamentId = -1;

	let tournaments = [];

	let state = STATE.HOME;

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
		tournaments.forEach(t => {
			if (t.id == newTournamentId) {
				t.selected = true;
				t.class = selectedStyle;
			}
			else {
				t.selected = false;
				t.class= notSelectedStyle;
			}
		});
	}

</script>

<div class="w3-panel w3-card startDialog">

		<p style="display:inline">
				<button on:click={() => changeState(STATE.HOME)}>Accueil</button>
				<button on:click={() => changeState(STATE.BUILD)}>Configuration</button>
				<button on:click={() => changeState(STATE.RUN)}>Jeu</button>	 			
			</p>
</div>




{#if state == STATE.HOME}

<ul class="w3-ul w3-border">
{#each tournaments as tournament}
	<li on:click={() => {setTournament(tournament.id);}} class={tournament.class}>
		
		<span>{tournament.name}</span><br>
		<span><i>{tournament.date}</i></span>
	</li>
{/each}
<li on:click={() => {setTournament(-1);}} class={notSelectedStyle}>		
		<span>nouveau tournoi</span><br>		
	</li>
</ul>

{/if}


{#if (state == STATE.BUILD)}
	<Build tournamentId={tournamentId}></Build>
{:else}
	<Run tournamentId={tournamentId}></Run>
{/if}

