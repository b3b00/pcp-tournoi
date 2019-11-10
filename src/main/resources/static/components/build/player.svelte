<style type="text/scss">
    @import "../../styles/global.scss";
    button.hide {
        display:none;
        visibility:hidden;
    }    
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import { alertError } from '../alertStore.js';
    import {tools} from '../tools.js';

    const dispatch = createEventDispatcher();

    export let id;
    export let name;
    export let isLicensed;
    export let tournamentId;
    export let isNewPlayer = false;
    export let edited = false;

    let idOrig;
    let nameOrig;
    let isLicensedOrig;
    let isNewPlayerOrig; 



    const save = "fa fa-check awesome";
    const nosave = "hide";

    const cancel = "fa fa-close awesome";
    const nocancel = "hide";

    const trash = "fa fa-trash awesome";
    const notrash = "hide";


    let trashClass = "fa fa-trash awesome";
    let saveClass = "fa fa-check awesome";
    let cancelClass = "fa fa-close awesome";

    

    function computeStyles() {        
        saveClass = edited  ? save : nosave;
        cancelClass = edited  ? cancel : nocancel;
        trashClass = isNewPlayer ? notrash : trash;
    }

    onMount(async () => {
        idOrig = id;
        nameOrig = name;
        isLicensedOrig = isLicensed;
        isNewPlayerOrig = isNewPlayer;
        computeStyles();
    }
    );

    function toggleLicensed() {
        isLicensed = !isLicensed;
        edited = true;
        computeStyles();        
    }

    function onNameChange() {
        edited = true;
        computeStyles();
    }

    async function onSave() {
        let data = {
            "isLicensed" : isLicensed,
            "name" : name         
        }
        if (!isNewPlayer) {
            data["id"] = id;
        }

        let httpMethod = isNewPlayer ? "POST" : "PUT";
        const res = await fetch(`/tournament/${tournamentId}/players`,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: httpMethod,
                body: JSON.stringify(data)
            });
        if (res.status >= 200 && res.status <= 299) {
            data = await res.json();
            name = "";
            isLicensed = data.isLicensed;
            edited = false;
            computeStyles();
            dispatch("change", { 'tournamentId': tournamentId });
        }
        else {
            const body = await res.json();
            if (tools.notNullOrUndefined(body)) {
                alertError(`Erreur : ${body.message}`);
            }
        }            
    }

    async function onDelete() {
        if (!isNewPlayer) {
            const res = await fetch(`/tournament/${tournamentId}/players/${id}`,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "DELETE"
            })
            .then(function (res) {
                
                        name = "";
                        isLicensed = false;
                        edited = false;
                        isNewPlayer = true;
                        computeStyles();
                        dispatch("change", { 'tournamentId': tournamentId })
                    }
                )
            .catch(function (res) {
                // TODO
            });
        }
    }

    async function onCancel() {
        name = nameOrig;
        isLicensed = isLicensedOrig;
        edited = false;
        isNewPlayer = isNewPlayerOrig;
        computeStyles();
    }


</script>

    <td>
    <input class="w3-input" style="width:100%" on:change={onNameChange} type="text" name = "name" id="name"  bind:value={name}/>
</td>
<td>
    <input class="w3-input"  type="checkbox" style="font-size:24px;height:24px;" bind:checked={isLicensed} on:click={() => {toggleLicensed();}}/>
</td>
<td>
        <button class="w3-button pcp-color1 w3-ripple {trashClass}" on:click={onDelete} style="font-size:24px;">&nbsp;</button>
</td>

<td  >
        <button class="w3-button pcp-color1 w3-ripple {saveClass}" on:click={onSave} style="font-size:24px;">&nbsp;</button>
</td>

<td style="cursor: pointer;" >
        <button class={cancelClass} on:click={onCancel} style="font-size:24px;">&nbsp;</button>
</td>