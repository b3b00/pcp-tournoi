<script>

    import Team from './team.svelte';
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let teams = [];
    export let tournamentId = -1;

    onMount(async () => {
        let myTeam = {
            player1: {
                name: "licencié",
                isLicensed: true,
                id: 1
            },
            player2: {
                name: "pas licencié",
                isLicensed: false,
                id: 2
            }
        };

        
        teams.push(myTeam);
        teams = teams;

        if (tournamentId != -1) {
            await load();
        }

    });

    async function load() {

    }



    function toPlayers() {
        dispatch("back", { 'tournamentId': tournamentId })
    }



</script>

{#each teams as team}
<Team team={team}/>
{/each}