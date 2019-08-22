<!-- preview group (ranked) in group phase view -->
<!-- clickable : opens group detail -->
<!-- uses :
  - 0
-->

<style>
  </style>
  
  <script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
  
    import MatchPreview from './match_preview.svelte';

    const dispatch = createEventDispatcher();
  
    export let groupPlay;
  
    onMount(() => {
      
    })
  
    function openGroup() {
      dispatch("open", { 'group': groupPlay })
    }

    function openMatch() {

    }
  
  </script>
  

  <div on:click={openGroup}>

    <ul>
      <li><h2>group {groupPlay.group.name}}</h2></li>
      {# each groupPlay.rankings ranking} 
        <li>{ranking.team.name} - {ranking.points} </li>
      {/each}
    </ul>
  </div>