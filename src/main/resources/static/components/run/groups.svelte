<!-- display groups preview for group phase-->
<!-- uses :
  - group_preview   
-->

<script>

  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import GroupPreview from './group_preview.svelte';
  
  export let phase;

</script>
{#if phase !== null && phase !== undefined}
  {#each phase.groups as group} 
      <GroupPreview groupPlay={group}></GroupPreview>
  {/each}
{/if}
