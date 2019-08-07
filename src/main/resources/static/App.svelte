<script>

const states = {
	CONFIG : "config",
	PLAYERS : "players",
	TEAMS : "teams",
	GROUPS : "groups"
}

	import Config from './components/config.svelte';
	import Players from './components/players.svelte';
	import Teams from './components/teams.svelte';
	import Groups from './components/groups.svelte';
		
	let state=states.CONFIG;
	let tournamentId = -1;

	function onConfig(data) {
		tournamentId = data.detail.tournamentId;
		state=states.CONFIG;
	}

	function onPlayers(data) {
		tournamentId = data.detail.tournamentId;
		state= states.PLAYERS;
	}

	function onTeams(data) {
		tournamentId = data.detail.tournamentId;
		state= states.TEAMS;
	}

</script>

{#if (state == states.CONFIG) }

	<Config on:done="{onPlayers}"/>

{:else if (state == states.PLAYERS)}

	<Players tournamentId={tournamentId} on:back={onConfig} on:next={onTeams}/>

{:else if (state == states.TEAMS)}

	<Teams on:back={onPlayers}/>

{:else}
<p>unknwon state <strong>{state}</strong></p>
{/if}
