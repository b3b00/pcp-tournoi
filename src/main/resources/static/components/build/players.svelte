<style type="text/scss">
    @import "../../styles/global.scss";
</style>
<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import Player from './player.svelte';
    import Dropzone from "svelte-dropzone/dropzone.svelte";

    const dispatch = createEventDispatcher();

    let tournament = {};
    let players = [];
    export let tournamentId = -1;

    onMount(async () => {
        await load();
    });

    async function load() {
        if (tournamentId != -1) {
            const res = await fetch(`/tournaments/${tournamentId}`);
            tournament = await res.json();
            players = tournament.players;
            if (players === undefined) {
                players = [];
            }
            countLic = await countLicensees();
            countNonLic = await countNotLicensees()
        }
    }

    let countLic = 0;
    let countNonLic = 0;    

    async function countLicensees() {
        let count = 0;
        if (players !== undefined && players !== null) {
            players.forEach(p => {
                if (p.isLicensed) {
                    count++;
                }
            });
        }
        return count;
    }


    async function openFile() {
        const f = document.getElementById("filer");
        f.click();
    }

    async function changeFiles(event) {
        const f = event.target;
        if (f !== null && f !== undefined) {
            if (f.files.length == 1) {
                await addedfile(f.files[0]);
                f.files = null;
                f.value= null;
            }
            else if (f.files.length > 1) {                
                f.files = null;
                f.value = null;
            }
        }    
    }


    async function countNotLicensees() {
        return players !== undefined ? players.length - countLic : 0;

    }
    async function addedfile (file) {
        const formData = new FormData();

        formData.append('file', file);

        const options = {
            method: 'POST',
            body: formData,
        };

        const res = await fetch(`tournaments/${tournamentId}/players/upload`, options);
        if (res.status >= 200 && res.status <= 299) {
            tournament = await res.json();
            players = tournament.players;
            countLic = await countLicensees();
            countNonLic = await countNotLicensees()
        }
        else {
            body = await res.json();
            window.alert("Erreur lors de l'import : "+res.status+"\n"+body.message);
        }
        
    }

    function exportPlayers() {        
        const downloader = document.getElementById("downloader");
        downloader.src=`/tournaments/${tournamentId}/players/download`;
    }


</script>


  


<p>{countLic + countNonLic} joueurs</p>
<p>{countLic} confirm&eacute;s</p>
<p>{countNonLic} d&eacute;butants</p>
<span tooltip="télécharger la liste des joueurs" class="fa fa-download w3-xxlarge" style="cursor:pointer" 
on:click={exportPlayers}>
        &nbsp;
        </span>
        
<iframe title="downloader" id="downloader" style="display:none">&nbsp;</iframe>

<span tooltip="envoyer la liste des joueurs" class="fa fa-upload w3-xxlarge" style="cursor:pointer" 
on:click={() => {openFile()}}
>
<input style="display:none" type="file" id="filer" on:change={changeFiles}/>
        &nbsp;
        </span>


<table class="w3-table-all w3-centered w3-card-4 w3-small " style="width: 50%;margin: auto;">
    <tr>
        <th>Nom</th>
        <th>confirm&eacute; ?</th>
        <th>&nbsp;</th><!-- delete -->
        <th>&nbsp;</th><!-- save -->
        <th>&nbsp;</th><!-- cancel -->
    </tr>
    <tr>
        <Player isNewPlayer={true} name="" id="" isLicensed="false" tournamentId={tournamentId} on:change={load}
            edited={false}></Player>
    </tr>
    {#each players as p (p.id)}
<tr>
    <Player {...p} tournamentId={tournamentId} on:change={load} isNewPlayer={false} edited={false}/>
</tr>

{/each}
</table>

<!-- </Dropzone> -->

<hr/>




