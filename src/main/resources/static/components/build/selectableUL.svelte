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

    const dispatch = createEventDispatcher();

    let selectClass = "w3-display-container unselected header";

    onMount(async () => {
        setHeaderStyle();   
    });

    export let selected = false;

    export let payload = {};

    export let label = "";

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

    function dropped(ev) {
        dispatch("drop",{event:ev,payload:payload});
    }

</script>
<ul class="w3-ul w3-border w3-card" on:drop={dropped} on:dragover={(e) => {e.preventDefault();}}>
    <li class="{selectClass}" on:click={selectItem} >{label != null ? label : "&nbsp;"}

    </li>
    <slot></slot>
</ul>