<style>
    div.startDialog {
        width: 30%;
        margin: auto;
        /* background-color: aqua;*/
    }
</style>

<script>

    import { onMount } from 'svelte';
    import { createEventDispatcher } from 'svelte';
    import { beforeUpdate, afterUpdate } from 'svelte';    

    import Select from 'svelte-select';

    const dispatch = createEventDispatcher();

    let options = [];

    let optionsByName = [];

    let tournamentName = "";

    let tournament = {}

    let tournamentDate = new Date();

    export let tournamentId = -1;


    let tournamentOptions = {
        mode: "SINGLE",
        winningSets: 3,
        setLength: 11
    };

    let tournaments = [];

    let tournamentsById = {};

    let tournamentItems = [];

    function changeMode() {
        var modeNode = document.getElementsByName("mode");

        var newMode = optionsByName[tournamentOptions.mode];
        tournamentOptions.winningSets = newMode.winningSets;
        tournamentOptions.setLength = newMode.setLength;
        tournamentOptions.id = newMode.id;
        tournamentOptions.isPreset = newMode.isPreset;

    }

    function unpreset() {
        tournamentOptions.id = -1;
        tournamentOptions.isPreset = false;
    }

    function loadTournament(id) {
        fetch(`/tournaments/${id}`,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "GET"
            })
            .then(function (res) {
                res.json().then(
                    function (tournament) {
                        tournamentId = tournament.id;
                        const opt = tournament.options;
                        tournamentOptions = opt;                        
                        tournamentName = tournament.name;
                        tournamentDate = tournament.date;
                        dispatch("setTournament", { 'tournamentId': id })
                    }
                );

            })
            .catch(function (res) {
                // TODO
            })
    }

    function saveTournament() {
            
        if (tournamentName.length === undefined || tournamentName == null || tournamentName.length == 0) {
            alert('Vous devez donner un nom au tournoi');
            return;
        }
        var data = {
            "name": tournamentName,
            "date":tournamentDate,
            "options": tournamentOptions            
        };
        let uri = "/tournament/options/";
        let httpMethod = "POST";
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {       
            uri = `/tournament/${tournamentId}/options/`
            data.id= tournamentId;
            httpMethod = "PUT";
        }
        fetch(uri,
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: httpMethod,
                body: JSON.stringify(data)
            })
            .then(function (res) {
                res.json().then(
                    function (id) {
                        openTournament(id);
                    }
                );

            })
            .catch(function (res) {
                // TODO
            })
    }

    async function openTournament (id) {
        tournamentId = id;
        
        dispatch("setTournament", { 'tournamentId': id })
    }


    async function fetchPresetOptions() {
        const res = await fetch(`/options/preset`);
        options = await res.json();
        options.forEach(opt => {
            optionsByName[opt.mode] = opt;
        });
        tournamentOptions = optionsByName["SINGLE"];
    }

    // async function fetchTournaments() {
    //     const res = await fetch('/tournaments');
    //     tournaments = await res.json();
    //     tournaments.forEach(t => {
    //         tournamentsById[t.id] = t;
    //         tournamentItems.push({ "label": `${t.name}`, "value": `${t.id}` });
    //     })
    // }

    async function fetchTournament(id) {
        const res = await fetch(`/tournaments/${id}`);
        tournament = await res.json();
        tournamentId = tournament.id;
        tournamentName = tournament.name;
        tournamentDate = tournament.date;
        tournamentOptions = tournament.options;
    }

    onMount(async () => {
        fetchPresetOptions();
        if (tournamentId !== undefined && tournamentId !== null && tournamentId != -1) {
            fetchTournament(tournamentId);
        }
    });

</script>



<div class="w3-panel w3-card startDialog" >

<div class="w3-row-padding">

    <div class="w3-half">  
    <label for="name">Nom : </label>
    <input type="text" name = "name" id="name" class="w3-input" bind:value={tournamentName}/>
    </div>


    <div class="w3-half">
    <label for="name">Date : </label>
    <input type="date" name = "date" id="date" class="w3-input" bind:value={tournamentDate}/>
    </div>
</div>

<label>Mode :</label>
{#each options as option, y}
<input class="w3-radio" type="radio" name="mode" on:change="{changeMode}" bind:group={tournamentOptions.mode} value="{option.mode}">{option.label}<br>
{/each}
<br/>

<div class="w3-row-padding">

    <div class="w3-half">
<label for="winningSets" >sets gagnants :</label>
<input class="w3-input" type="number" id="winningSets" placeholder="3" min="1" max="3" bind:value={tournamentOptions.winningSets} on:change={unpreset}/>  
<br/>
    </div>

<div class="w3-half">
<label for="setlength">nombre de points par sets</label> 
<input class="w3-input" type="number" id="setlength" placeholder="11" min="3" max="100" bind:value={tournamentOptions.setLength} on:change={unpreset}/>  
</div>
</div>
<br/>
<button on:click={saveTournament} >Enregistrer</button>


</div>