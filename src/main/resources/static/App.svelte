<script>

const states = {
	CONFIG : "config",
	PLAYERS : "players",
	TEAMS : "teams",
	GROUPS : "groups"
}


	// layout
	import Layout from './components/layout/Layout.svelte';

	// screens
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


	function onGroups(data) {
		tournamentId = data.detail.tournamentId;
		state= states.GROUPS;
	}

	function setTournament(data) {
		tournamentId = data.detail.tournamentId;
	}

</script>

<Layout on:one="{onConfig}" on:two="{onPlayers}" on:three="{onTeams}" on:four="{onGroups}" tournamentId={tournamentId}>
	{#if (state == states.CONFIG) }
		<Config ournamentId={tournamentId} on:setTournament={setTournament}/>
	{:else if (state == states.PLAYERS)}
		<Players tournamentId={tournamentId} />
	{:else if (state == states.TEAMS)}	
		<Teams tournamentId={tournamentId} />
	{:else if (state == states.GROUPS)}
		<Groups tournamentId={tournamentId} />
	{:else}
		<p>unknwon state <strong>{state}</strong></p>
	{/if}
</Layout>
