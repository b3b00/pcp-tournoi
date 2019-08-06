<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let id;
    export let name;
    export let isLicensed;
    export let tournamentId;
    export let isNewPlayer = false;
    let isLicensedClass = "";

    function computeStyles() {
        isLicensedClass = isLicensed ? "fa fa-check-square-o" : "fa fa-square-o";
    }

    onMount(async () => {
        computeStyles();
    }
    );

    function toggleLicensed() {
        isLicensed = !isLicensed;
        computeStyles();
    }

</script>

    <td>
    <input style="width:100%" type="text" name = "name" id="name"  bind:value={name}/>
</td>
<td on:click={() => {toggleLicensed();}} style="cursor: pointer;">
    <span style="font-size:24px;" 
    class={isLicensedClass}
    > &nbsp;</span>
    
</td>
<td style="cursor: pointer;">
        <span class="fa fa-trash" style="font-size:24px;">&nbsp;</span>
</td>