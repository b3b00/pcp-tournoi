<style type="text/scss">
  @import "../../styles/global.scss";
  .clickable {
    cursor:pointer;
  }
  .column {
    width:25%;
    float:left;
    margin-right: 5%;
  }
  .item {
    padding-bottom: 5px;
    text-decoration: underline;
    text-align: center;
  }
</style>
<script>

  import { tools } from './tools.js';
  import { onMount } from 'svelte';
  import { createEventDispatcher } from 'svelte';


  const dispatch = createEventDispatcher();

  export let tournament;

  export let tournamentId;

  //let moveMe;

  onMount(async () => {
    //moveMe = tools.mover(dispatch);    
  });


  function openSection(id) {
    var x = document.getElementById(id);
    if (x.className.indexOf("w3-show") == -1) {
      x.className += " w3-show";
    } else {
      x.className = x.className.replace(" w3-show", "");
    }
  }

</script>


  <button on:click={()=> {openSection('Groups')}} class="w3-btn w3-block w3-black w3-left-align">Poules</button>
  <div id="Groups" class="w3-container w3-hide">
    <h4>Poule A</h4>
    <h4>Poule B</h4>
  </div> <!-- poules -->

  <button on:click={()=> {openSection('Boards')}} class="w3-btn w3-block w3-black w3-left-align">Tableaux</button>
  <div id="Boards" class="w3-container w3-hide">
    <button on:click={()=> {openSection('RunningBoards')}} class="w3-btn w3-block w3-black w3-left-align">Tableaux en cours</button>
    <div id="RunningBoards" class="w3-container w3-hide">
      <h4>principal</h4>
      <h4>un autre</h4>
    </div> <!-- tableaux en cours -->

    <button on:click={()=> {openSection('EndedBoards')}} class="w3-btn w3-block w3-black w3-left-align">Tableaux finis</button>
    <div id="EndedBoards" class="w3-container w3-hide">
      <h4>consolante</h4>
      <h4>encore un autre</h4>
    </div> <!-- tableaux finis -->
  </div> <!-- tableaux-->