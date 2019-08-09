<style>
    button.hide {
        display:none;
        visibility:hidden;
    }
    input.hide {
        display:none;
        visibility:hidden;
    }
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';

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

    let isLicensedClass = "";

    const save = "fa fa-check";
    const nosave = "hide";

    const cancel = "fa fa-close";
    const nocancel = "hide";

    const trash = "fa fa-trash";
    const notrash = "hide";


    let trashClass = "fa fa-trash";
    let saveClass = "fa fa-check";
    let cancelClass = "fa fa-close";

    

    function computeStyles() {
        isLicensedClass = isLicensed ? licensed : notlicensed;
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
        const res = fetch(`/tournament/${tournamentId}/players`,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: httpMethod,
                body: JSON.stringify(data)
            }).then(function (res) {
                res.json().then(                    
                    function (data) {
                        name = "";
                        isLicensed = data.isLicensed;
                        edited = false;
                        computeStyles();
                        dispatch("change", { 'tournamentId': tournamentId })
                    }
                );
            })
            .catch(function (res) {
                console.log("ERROR");
                console.log(res);
            });
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
                console.log("ERROR");
                console.log(res);
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
    <input style="width:100%" on:change={onNameChange} type="text" name = "name" id="name"  bind:value={name}/>
</td>
<td>
    <input type="checkbox" style="font-size:24px;height:24px;"  on:click={() => {toggleLicensed();}}/>
    
    
</td>
<td>
        <button class={trashClass} on:click={onDelete} style="font-size:24px;">&nbsp;</button>
</td>

<td  >
        <button class={saveClass} on:click={onSave} style="font-size:24px;">&nbsp;</button>
</td>

<td style="cursor: pointer;" >
        <button class={cancelClass} on:click={onCancel} style="font-size:24px;">&nbsp;</button>
</td>