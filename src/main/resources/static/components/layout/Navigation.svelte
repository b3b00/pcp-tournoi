
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
	
		
	
	</script>
	<p style="display:inline">
		<button on:click={one}>config</button><button on:click={two}>players</button><button on:click={three}>teams</button>
	</p>
	