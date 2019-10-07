
<style type="text/scss">
    @import "../../styles/global.scss";
</style>
<script>

    const states = {
        CONFIG : "config",
        PLAYERS : "players",
        TEAMS : "teams",
        GROUPS : "groups"
    }
    
    
        // layout
        import Layout from './layout/Layout.svelte';
    
        // screens
        import Config from './config.svelte';
        import Players from './players.svelte';
        import Teams from './teams.svelte';
        import Groups from './groups.svelte';
    
        import { onMount } from 'svelte';    
            
        let state=states.CONFIG;
        export let tournamentId = -1;
    

        onMount(async () => {
            tournamentId = tournamentId;
        });

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
            <Config tournamentId={tournamentId} on:setTournament={setTournament}/>
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
    