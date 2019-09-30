<style>
	li.selected {
		background-color: lightgray;
	}

	li.selectable {
		cursor: pointer;
	}

	button.selected {
		background-color: lightgray;
		font-weight: bold;
	}

	button.notSelected {
		background-color: white;
		font-weight: normal;
	}
</style>
<script>
	const notSelectedStyle = "w3-bar selectable";
	const selectedStyle = notSelectedStyle + " selected";

	const STATE = {
		BUILD: "build",
		RUN: "run",
		HOME: "home"
	}

	import { onMount } from 'svelte';
	import { createEventDispatcher } from 'svelte';
	import { beforeUpdate, afterUpdate } from 'svelte';

	import Build from './components/build/Build.svelte';
	import Run from './components/run/run.svelte';
	import { tools } from './components/run/tools.js';


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

	async function changeState(newState) {
		if (newState == STATE.HOME) {
			await  fetchTournaments();
		}
		state = newState;
	}

	function setTournament(newTournamentId) {
		tournamentId = newTournamentId;
		if (tournaments != null && tournaments.length > 0) {
			tournaments.forEach(t => {
				if (t.id == newTournamentId) {
					t.selected = true;
					t.class = selectedStyle;
				}
				else {
					t.selected = false;
					t.class = notSelectedStyle;
				}
			});
		}
		if (newTournamentId == -1) {
			state = STATE.BUILD;
		}
		tournaments = tournaments;
	}

	async function deleteTournament(id) {
		tournaments = await tools.deleteTournament(id);
	}

</script>

<div class="w3-panel w3-card startDialog">

	<p style="display:inline">
		<button class={(state==STATE.HOME) ? "selected w3-quarter" : "notSelected w3-quarter" } on:click={()=>
			changeState(STATE.HOME)}>Accueil</button>
		<button class={(state==STATE.BUILD) ? "selected w3-quarter" : "notSelected w3-quarter" } on:click={()=>
			changeState(STATE.BUILD)}>Configuration</button>
		<button class={(state==STATE.RUN) ? "selected w3-quarter" : "notSelected w3-quarter" } on:click={()=>
			changeState(STATE.RUN)}>Jeu</button>
		<a href="help/help.html" class="fa fa-question-circle" style="font-size: 28px; margin-left:48px" >&nbsp;</a>
	</p>
</div>




{#if state == STATE.HOME}


<ul class="w3-ul w3-border">
{#if tournaments != null && tournaments.length > 0}
	{#each tournaments as tournament}
		<li on:click={() => {setTournament(tournament.id);}} class={tournament.class}>
			
			<span>{tournament.name}</span><br>
			<span><i>{tournament.date}</i></span>
			<span on:click="{() => {deleteTournament(tournament.id)}}" class="w3-button" >&times;</span>
		</li>
	{/each}
{/if}
<li on:click={() => {setTournament(-1);}} class={tournamentId == -1 ? selectedStyle : notSelectedStyle}>		
		<span>nouveau tournoi</span><br>		
	</li>
</ul>

{/if}


{#if (state == STATE.BUILD)}
	<Build tournamentId={tournamentId}></Build>
{/if}
{#if (state == STATE.RUN)}
	<Run tournamentId={tournamentId}></Run>
{/if}