
<script>
	import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

	const dispatch = createEventDispatcher();

	export let tournamentId=-1;

	function one() {
		dispatch("one", { 'tournamentId': tournamentId })
	}
	
	function two() {
		dispatch("two", { 'tournamentId': tournamentId })
	}

	function three() {
		dispatch("three", { 'tournamentId': tournamentId })
	}
	
	function four() {
		dispatch("four", { 'tournamentId': tournamentId })
	}
		
	
	</script>
	<p style="display:inline">
		<button on:click={one}>configuration</button>
		<button on:click={two}>joueurs</button>
		<button on:click={three}>équipes</button>
		<button on:click={four}>poules</button>
	</p>
	