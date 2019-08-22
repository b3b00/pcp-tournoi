<style>
</style>

<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let items = {}

    function onClick(index) {
        // let newCrumb = items;
        // if (index > 0) {
            let newCrumb = items.slice(0,index+1);
        // }
        dispatch("click", { 'items': newCrumb });
    }

</script>

{#each items as item,i}

    <span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;</span>
    <span style="cursor: pointer" on:click={() => {onClick(i)}}>{item.label}</span>
    

{/each}