<style>
    .bread-item {
        cursor:pointer;   
        float:left;     
    }
    .bread-item:hover {
        background-color: cyan;
    }
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

<div class="w3-cyan w3-bar">
        <div class="w3-bar-item w3-teal w3-hover-cyan" style='font-weight:bold;cursor: pointer;'>Configuration</div>
{#each items as item,i}

    <div class="w3-bar-item w3-cyan bread-item" on:click={() => {onClick(i)}}>
    <span class="fas fa-angle-right" style="" >&nbsp;</span>
    <span>{item.label}</span>
    </div>

{/each}
</div>