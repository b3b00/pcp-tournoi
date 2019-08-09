<style>
        li.header {
            cursor: pointer;
            font-size: 0.5em;
        }
        li.selected {
            background-color: lightgray;            
        }
        li.unselected {
            background-color: white;            
        }
    </style>
<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    //import Player from './player.svelte';

    const dispatch = createEventDispatcher();

    let selectClass = "w3-display-container unselected header";

    onMount(async () => {
        setHeaderStyle();   
    });

    export let selected = false;

    function setHeaderStyle() {
        if (selected) {
            selectClass = "w3-display-container selected header";
        }
        else {
            selectClass = "w3-display-container unselected header";
        }     
    }

    function selectItem() {
        selected = !selected;
        setHeaderStyle();
        dispatch("selectionChanged",{state:selected});
    }
</script>
<ul class="w3-ul w3-border w3-card" >
    <li class="{selectClass}" on:click={selectItem} >&nbsp;

    </li>
    <slot></slot>
</ul>