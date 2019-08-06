<script>

const states = {
	START : "start",
	PLAYERS : "players",
	GROUPS : "groups"
}

	import Start from './components/start.svelte';
	import Players from './components/players.svelte';
	import Groups from './components/groups.svelte';
		
	let state="start";
	let tournamentId = -1;

	function onStartDone(data) {
		console.log(data);
		tournamentId = data.detail.tournamentId;
		state="players";
	}

</script>

{#if (state == states.START) }

	<Start on:done="{onStartDone}"/>

{:else if (state == states.PLAYERS)}

	<Players tournamentId={tournamentId}/>

{:else if (state == states.GROUPS)}

	<Groups/>

{/if}
