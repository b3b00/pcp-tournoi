<style type="text/scss">
   
   @import "../../styles/global.scss";
    .bread-item {
        cursor:pointer;   
        float:left;     
    }
    .bread-item:hover {
        background-color:$color-2;
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

<div class="pcp-color2 w3-bar">
<!--{#each items as item,i}

    <div class="w3-bar-item pcp-color2 bread-item" on:click={() => {onClick(i)}}>
    <span class="fas fa-angle-right">&nbsp;</span>
    <span>{item.label}</span>
    </div>

{/each}-->
</div>