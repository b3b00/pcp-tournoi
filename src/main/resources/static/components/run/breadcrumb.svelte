<style>
</style>

<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let items = {}

    function onClick(index) {
        dispatch("click", { 'items': items.slice(0, index) });
    }

</script>

{#each items as item,i}

    <a href="#" on:click={() => {onClick(i)}}</a>}>{item.name}</a>

{/each}