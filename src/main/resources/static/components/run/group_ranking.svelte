<style type="text/scss">
@import "../../styles/global.scss";
</style>
<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    
    export let groupPlay;

    let scores = [];

    onMount(async () => {
    });


</script>
 <div>
    {# each groupPlay.rankings ranking} 
      <div>{ranking.team.name} - {ranking.points} </div>
    {/each}
  </div><div>
  {#each group.rankings as ranking}
  <div></div>
</div>
