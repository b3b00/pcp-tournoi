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

  let moveMe;


  onMount(async () => {
    moveMe = tools.mover(dispatch);    
  });

  function openGroup(groupPlay) {     
        moveMe("group", "poule "+groupPlay.group.name,"group",groupPlay.id);
    }


  function openSection(id) {
    var x = document.getElementById(id);
    if (x.className.indexOf("w3-show") == -1) {
      x.className += " w3-show";
    } else {
      x.className = x.className.replace(" w3-show", "");
    }
  }

   function getIsDoneflag(groupPlay) {
      let done =  "<span style='color:green' class='fa fa-check w3-display-center'></span>";
      return groupPlay.isDone ? done : "";
    }

</script>


  <button  on:click={()=> {openSection('Groups'); moveMe("groupPhase","poules","groupPhase",null)}} class="pcp-color1 w3-btn w3-block w3-black w3-left-align">Poules</button>
  <div id="Groups" class="w3-container w3-hide">
    {#if tournament !== null && tournament !== undefined && tournament.run !== undefined && tournament.run.groupPhase !== undefined && tournament.run.groupPhase.groups !== undefined}
      {#each tournament.run.groupPhase.groups as group} 
        <h6  class="pcp-hover-color2" on:click={() => {openGroup(group)}}>poule {group.group.name} {@html getIsDoneflag(group)}</h6>
      {/each}
    {/if}
  </div> <!-- poules -->

  <button  on:click={()=> {openSection('Boards')}} class="pcp-color1 w3-btn w3-block w3-black w3-left-align">Tableaux</button>
  <div id="Boards" class="w3-container w3-hide">

{#if (tools.guard(tournament,"run.board.boards"))}


<!-- {#if (tournament !== undefined && 
  tournament !== null &&
  tournament.run !== undefined && 
  tournament.run !== null && 
  tournament.run.board !== undefined && 
  tournament.run.board !== null && 
  tournament.run.board.boards !== undefined &&
  tournament.run.board.boards !== null
  )} -->



    <button   on:click={()=> {openSection('RunningBoards')}} class="pcp-color1 w3-btn w3-block w3-black w3-left-align">Tableaux en cours</button>
    <div id="RunningBoards" class="w3-container w3-hide">
      {#each tournament.run.board.boards as board}
        {#if (!board.isDone)}
          <h6 class="pcp-hover-color2" on:click={() => {moveMe("board", board.name,"board",board.id);}} style="cursor:pointer">{board.name}</h6>
        {/if}
      {/each}
    </div> <!-- tableaux en cours -->

    <button on:click={()=> {openSection('EndedBoards')}} class="pcp-color1 w3-btn w3-block w3-black w3-left-align">Tableaux finis</button>
    <div id="EndedBoards" class="w3-container w3-hide">
     {#each tournament.run.board.boards as board}
        {#if (board.isDone)}
          <h6 class="pcp-hover-color2" on:click={() => {moveMe("board", board.name,"board",board.id);}} style="cursor:pointer" >{board.name}</h6>
        {/if}
      {/each}
    </div> <!-- tableaux finis -->
    <div class="clickable item " on:click={() => {moveMe("newboard", "nouveau tableau","board",-1);}}>
  <span  class="w3-button w3-large w3-circle w3-xlarge w3-ripple pcp-color1" title="nouveau tournoi" style="z-index:0;cursor:pointer" href="#">+</span>
</div>

{/if}

  </div> <!-- tableaux-->