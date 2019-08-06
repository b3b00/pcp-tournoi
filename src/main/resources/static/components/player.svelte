<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let id;
    export let name;
    export let isLicensed;
    export let tournamentId;

</script>

    <td>
    <!-- <label for="name">Nom : </label> -->
    <input style="width:100%" type="text" name = "name" id="name"  bind:value={name}/><!--class="w3-input"-->
</td>
<td>
    <!-- <label for="licensed">Licencié ? </label> -->
    <input type="checkbox" name = "isLicensed" id="isLicensed"  bind:value={isLicensed}/> <!--class="w3-check"-->
</td>