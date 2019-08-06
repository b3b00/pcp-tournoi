<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let id;
    export let name;
    export let isLicensed;
    export let tournamentId;
    let isLicensedClass = "";

    function computeStyle() {
        isLicensedClass = isLicensed ? "fa fa-check" : "fa";
    }

    onMount(async () => {
        computeStyle();
    }
    );

    function toggleLicensed() {
        isLicensed = !isLicensed;
        computeStyle();
    }

</script>

    <td>
    <!-- <label for="name">Nom : </label> -->
    <input style="width:100%" type="text" name = "name" id="name"  bind:value={name}/><!--class="w3-input"-->
</td>
<td on:click={() => {toggleLicensed();}} style="cursor: pointer;">
    <span style="font-size:24px;" 
    class={isLicensedClass}
    > <!--on:click={() => {opentTournament(tournament.id);}}-->
    &nbsp;&nbsp;&nbsp;</span>
    
</td>
<td style="cursor: pointer;">
        <span class="fa fa-trash" style="font-size:24px;">&nbsp;</span>
</td>