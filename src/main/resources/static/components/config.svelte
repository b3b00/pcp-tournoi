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


    // beforeUpdate(() => {
	// 	if (tournamentId != -1) {
    //     }
	// });

	// afterUpdate(() => {
	// 	if (tournamentId != -1) {
    //     }
	// });

    const dispatch = createEventDispatcher();

    let options = [];

    let optionsByName = [];

    let tournamentName = "";


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

    }

    function saveTournament() {
        if (tournamentName.length === undefined || tournamentName == null || tournamentName.length == 0) {
            alert('Vous devez donner un nom au tournoi');
            return;
        }
        var data = {
            "name": tournamentName,
            "options": tournamentOptions
        };
        fetch("/tournament/options/",
            {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST",
                body: JSON.stringify(data)
            })
            .then(function (res) {
                res.json().then(
                    function (id) {
                        dispatch("done", { 'tournamentId': id })
                    }
                );

            })
            .catch(function (res) {
                console.log("ERROR");
                console.log(res);
            })
    }

    function openTournament (id) {
        dispatch("done", { 'tournamentId': id })
    }


    async function fetchPresetOptions() {
        const res = await fetch(`/options/preset`);
        options = await res.json();
        options.forEach(opt => {
            optionsByName[opt.mode] = opt;
        });
    }

    async function fetchTournaments() {
        const res = await fetch('/tournaments');
        tournaments = await res.json();
        tournaments.forEach(t => {
            tournamentsById[t.id] = t;
            tournamentItems.push({ "label": `${t.name}`, "value": `${t.id}` });
        })
        console.log(tournamentItems);

    }

    onMount(async () => {
        fetchPresetOptions();
        fetchTournaments();
    });


</script>


<div class="w3-panel w3-card startDialog">


    <ul class="w3-ul w3-border">
        <li>
            <h2>Names</h2>
        </li>
        {#each tournaments as tournament}
                <li class="w3-bar" style="cursor: pointer;" on:click={() => {openTournament(tournament.id);}}> <!--on:click={() => {opentTournament(tournament.id);}}>-->
                    {tournament.name}
                </li>
                
                {/each}
                </ul>
</div>

<div class="w3-panel w3-card startDialog" >
<label for="name">Nom : </label>
<input type="text" name = "name" id="name" class="w3-input" bind:value={tournamentName}/>

<label>Mode :</label>
{#each options as option, y}
<input class="w3-radio" type="radio" name="mode" on:change="{changeMode}" bind:group={tournamentOptions.mode} value="{option.mode}">{option.mode}<br>
{/each}
<br/>

<label for="winningSets" >sets gagnants :</label>
<input class="w3-input" type="number" id="winningSets" placeholder="3" min="1" max="3" bind:value={tournamentOptions.winningSets}/>  
<br/>

<label for="setlength">nombre de points par sets</label> 
<input class="w3-input" type="number" id="setlength" placeholder="11" min="3" max="100" bind:value={tournamentOptions.setLength}/>  
<br/>

<button on:click={saveTournament} >C'est parti...</button>
</div>