
<style>
    div.startDialog {
                width: 30%;
                margin: auto;
                background-color: aqua;
    }
</style>

<script>

import { onMount } from 'svelte';

let options = [];

let optionsByName = [];

let tournamentName = "";


let tournamentOption = {
    mode:"SINGLE",
    winningSets:3,
    setLength:11
};

function changeMode() {
    var modeNode = document.getElementsByName("mode");
    
    var newMode = optionsByName[tournamentOption.mode];
    tournamentOption.winningSets = newMode.winningSets;
    tournamentOption.setLength = newMode.setLength;


}

onMount(async () => {
		const res = await fetch(`/options/preset`);
        options = await res.json();
        options.forEach(opt => {
            optionsByName[opt.mode] = opt;
        });
        console.log(optionsByName);
	});

</script>

<div class="w3-panel w3-card startDialog" >z
<label for="name">Nom : </label>
<input type="text" name = "name" id="name" class="w3-input" bind:value={tournamentName}/>

<label>Mode :</label>
{#each options as option, y}
<input class="w3-radio" type="radio" name="mode" on:change="{changeMode}" bind:group={tournamentOption.mode} value="{option.mode}">{option.mode}<br>
{/each}
<br/>

<label for="winningSets" >sets gagnants :</label>
<input class="w3-input" type="number" id="winningSets" placeholder="3" min="1" max="3" bind:value={tournamentOption.winningSets}/>  
<br/>

<label for="setlength">nombre de points par sets</label> 
<input class="w3-input" type="number" id="setlength" placeholder="11" min="3" max="100" bind:value={tournamentOption.setLength}/>  
<br/>

<button>C'est parti...</button>
</div>

