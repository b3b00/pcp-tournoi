<style type="text/scss">
    @import "styles/global.scss";
	li.selected {
		background-color: lightgray;
	}

	li.selectable {
		cursor: pointer;
	}
</style>
<script>
	const notSelectedStyle = "w3-bar-item pcp-hover-color1";
	const selectedStyle = "w3-bar-item pcp-color1 pcp-hover-color3";

	const STATE = {
		BUILD: "build",
		RUN: "run",
		HOME: "home"
	}

	import { onMount } from 'svelte';
	import { createEventDispatcher } from 'svelte';
	import { beforeUpdate, afterUpdate } from 'svelte';

	import { alertError } from './components/alertStore.js';
	import { tournament, setGlobalTournament } from './components/tournamentStore.js';
	import Build from './components/build/Build.svelte';
	import Run from './components/run/run.svelte';
	import { tools } from './components/tools.js';
	import Toast  from './components/Toast.svelte';

	let tournamentId = -1;

	let tournaments = [];

	let user = {};

	let state = STATE.HOME;

	onMount(async () => {
		fetchTournaments();
		fetchUser();
		if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
			fetchTournament(tournamentId);
		}
	});

	async function fetchTournaments() {
		const res = await fetch('/tournaments');
		if (res.status >= 200 && res.status <= 299) {
			tournaments = await res.json();
		}
		else {
			body = await res.json();
			alertError(`erreur lors du chargement des tournois ${res.status}<br/>${body.message}`)
		}
	}

	async function fetchUser() {
		const res = await fetch('/user');
		if (res.status >= 200 && res.status <= 299) {
			user = await res.json();
		}
		else {
			body = await res.json();
			alertError(`erreur lors du chargement de l'utiliseur ${res.status}<br/>${body.message}`)
		}
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
					setGlobalTournament(t);
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
		setGlobalTournament(undefined);
	}

</script>

<div class="w3-bar pcp-color1 w3-display-container">		
		<div class={(state==STATE.HOME) ? selectedStyle : notSelectedStyle } on:click={()=>
			changeState(STATE.HOME)}>Accueil</div>
		<div class={(state==STATE.BUILD) ? selectedStyle : notSelectedStyle } on:click={()=>
			changeState(STATE.BUILD)}>Configuration</div>
		<div class={(state==STATE.RUN) ? selectedStyle : notSelectedStyle } on:click={()=>
			changeState(STATE.RUN)}>Jeu</div>
		
		<div class="w3-xlarge w3-bar-item pcp-hover-color1 w3-center w3-display-middle">{#if ($tournament !== undefined && $tournament.name !== undefined)}{$tournament.name}{/if}</div>

		<a  href="help/help.html" class="w3-bar-item fa fa-question-circle" style="color:white;font-size: 28px; margin-left:48px;float:right" >&nbsp;</a>

		<span class="w3-bar-item" style="float:right">{#if tools.guard(user,"name")}{user.name}{/if}</span>
		<a  href="/logout" class="w3-bar-item fa fa-sign-out" style="color:white;font-size: 28px; margin-left:48px; float:right;" >&nbsp;</a>

	
</div>
<Toast/>




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
</ul>

<span on:click={() => {setTournament(-1);}} class="w3-button w3-large w3-circle w3-xlarge w3-ripple pcp-color1" title="nouveau tournoi" style="z-index:0;cursor:pointer" href="#">+</span>

{/if}


{#if (state == STATE.BUILD)}
	<Build tournamentId={tournamentId}></Build>
{/if}
{#if (state == STATE.RUN)}
	<Run tournamentId={tournamentId}></Run>
{/if}