<script>

import { onMount } from 'svelte';
import { createEventDispatcher } from 'svelte';

const dispatch = createEventDispatcher();

export let id;
export let name;
export let isLicensed;
export let tournamentId;
</script>

<div>    
    <label for="name">Nom : </label>
    <input type="text" name = "name" id="name" class="w3-input" bind:value={name}/>
    <label for="licensed">Licencié ? </label>
    <input type="checkbox" name = "isLicensed" id="isLicensed" class="w3-check" bind:value={isLicensed}/> -->
</div>
