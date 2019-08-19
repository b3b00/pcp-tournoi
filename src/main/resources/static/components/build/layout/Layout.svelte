<script>    
	import Navigation from './Navigation.svelte';
	export let tournamentId;
</script>	
<div>
<Navigation on:one on:two on:three on:four tournamentId={tournamentId}/>
<slot></slot>
</div> 
