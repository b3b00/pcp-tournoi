<!-- display groups preview for group phase-->
<!-- uses :
  - group_preview   
-->
<style>
.refresh {
  cursor:pointer;
  font-size: x-large;
  padding-left: 16px;
  padding-bottom: 16px;
}
</style>
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

  async function refresh() {
    let phaseId = phase.id;
    let newPhase = await tools.fetchGroupPhase(phaseId);  
    phase = newPhase;
  }

</script>
<div class="w3-container" style="clear:both">
<span  class='refresh fa fa-refresh w3-display-center' on:click={refresh}>&nbsp;</span>
</div>
{#if phase !== null && phase !== undefined}
<div class="w3-container">
  {#each phase.groups as group} 
      <GroupPreview groupPlay={group} on:move></GroupPreview>
  {/each}
</div>
{/if}
