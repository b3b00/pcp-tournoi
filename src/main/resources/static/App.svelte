<script>

const states = {
	START : "start",
	PLAYERS : "players",
	GROUPS : "groups"
}

	import Config from './components/config.svelte';
	import Players from './components/players.svelte';
	import Teams from './components/teams.svelte';
	import Groups from './components/groups.svelte';
		
	let state="start";
	let tournamentId = -1;

	function onConfigDone(data) {
		tournamentId = data.detail.tournamentId;
		state="players";
	}

</script>

{#if (state == states.START) }

	<Config on:done="{onConfigDone}"/>

{:else if (state == states.PLAYERS)}

	<Players tournamentId={tournamentId}/>

{:else if (state == states.TEAMS)}

	<Teams/>

{/if}
