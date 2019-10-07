<style type="text/scss">

    @import "../../styles/global.scss";

        li.header {
            cursor: pointer;
            font-size: 0.5em;
        }
        li.selected {
            @extend .pcp-color1;         
            @extend .header;
        }
        li.notselected {
            background-color: white;            
            @extend .header;
        }
    </style>
<script>
    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();


    onMount(async () => {
    });

    export let selected = false;

    export let payload = {};

    export let label = "";


    function selectItem() {
        selected = !selected;
        dispatch("selectionChanged",{state:selected});
    }

    function dropped(ev) {
        dispatch("drop",{event:ev,payload:payload});
    }

</script>
<ul class="w3-ul w3-border w3-card" on:drop={dropped} on:dragover={(e) => {e.preventDefault();}}>
    <li class={selected ? "selected w3-display-container" :"notselected w3-display-container"} on:click={selectItem} >{label != null ? label : "&nbsp;"}

    </li>
    <slot></slot>
</ul>