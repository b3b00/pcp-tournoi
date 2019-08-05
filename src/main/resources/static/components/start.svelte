<!-- TODO
    préremplissage des champs sur sélection mode. chercher les param depuis le back ? => options predef

    -->	
<script>

function changeMode() {
    modeNode = document.getElementsByName("mode");
    console.log(modeNode.Value);
}
</script>

<label for="name">Nom : </label>
<input type="text" name = "name" id="name"/>

<input type="radio" name="mode" on:change="{changeMode}" value="single"> Simple<br>
<input type="radio" name="mode" on:change="{changeMode}" value="double"> Double<br>

<label for="winningSets" >sets gagnants :</label>
<input type="number" id="winningSets" placeholder="3" min="1" max="3"/>  
<label for="setlength">nombre de points par sets</label> 
<input type="number" id="setlength" placeholder="11" min="3" max="100"/>  

