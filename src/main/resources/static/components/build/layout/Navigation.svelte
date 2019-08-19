
<style>
button.selected {	
	font-weight: bold;
}
button.notSelected {
	font-weight: normal;
}
</style>
<script>
	import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

	onMount(() => {
		selectedIndex = 0;
		setClasses();
	});

	const dispatch = createEventDispatcher();

	export let tournamentId=-1;

	let selectedIndex = 0;

	let classes = []

	function setClasses() {
		classes = []
		for (let i = 0; i < 4; i++) {
			let style = "notSelected";
			if (i == selectedIndex) {
				style="selected";
			}
			classes.push(style);
		}
	}

	function one() {
		dispatch("one", { 'tournamentId': tournamentId })
		selectedIndex = 0;
		setClasses();
	}
	
	function two() {
		dispatch("two", { 'tournamentId': tournamentId })
		selectedIndex = 1;
		setClasses();
	}

	function three() {
		dispatch("three", { 'tournamentId': tournamentId })
		selectedIndex = 2;
		setClasses();
	}
	
	function four() {
		dispatch("four", { 'tournamentId': tournamentId })
		selectedIndex = 3;
		setClasses();
	}
		
	
	</script>
	<p style="display:inline">
		<button on:click={one} class={classes[0]}>configuration</button>
		<button on:click={two} class={classes[1]}>joueurs</button>
		<button on:click={three} class={classes[2]}>équipes</button>
		<button on:click={four} class={classes[3]}>poules</button>
	</p>
	