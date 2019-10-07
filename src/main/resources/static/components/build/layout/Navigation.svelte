
<style type="text/scss">
div.selected {	
	font-weight: bold;
	cursor: pointer;
}
button.notSelected {
	font-weight: normal;
	cursor: pointer;
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
			let style = "w3-bar-item pcp-hover-color1 notSelected";
			if (i == selectedIndex) {
				style="w3-bar-item pcp-color1 pcp-hover-color2 selected";
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
		<div class="w3-bar pcp-color2">
		<div on:click={one} class={classes[0]}>Configuration</div>
		<div on:click={two} class={classes[1]}>Joueurs</div>
		<div on:click={three} class={classes[2]}>&Eacute;quipes</div>
		<div on:click={four} class={classes[3]}>Poules</div>
		</div>
	