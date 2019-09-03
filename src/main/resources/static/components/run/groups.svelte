<!-- display groups preview for group phase-->
<!-- uses :
  - group_preview   
-->

<script>

  import {tools} from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';

  import GroupPreview from './group_preview.svelte';

  const dispatch = createEventDispatcher();

  let moveMe;

  onMount(() => {
        moveMe = tools.mover(dispatch);
    });
  
  export let phase;

</script>
{#if phase !== null && phase !== undefined}
<div class="w3-container">
  {#each phase.groups as group} 
      <GroupPreview groupPlay={group} on:move></GroupPreview>
  {/each}
</div>
{/if}
