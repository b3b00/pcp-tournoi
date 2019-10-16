<style>
  @import "../../styles/global.scss";

.error {
    background-color : red; 
    color: white;
    display: block;
}

.warn {
    background-color : orange; 
    color : white;
    display : block;
}

.info {
    background-color : green;
    color: white;
    display : block;
}

.hide {    
    display: block;
}





</style>
<script>
    import { onDestroy } from 'svelte';
    import { alertMessage, LEVEL } from './alertStore.js';

    let message;
    let toastClass;
    let displayed;

	const unsubscribe = alertMessage.subscribe(value => {
		message = value.message;
        displayed = value.displayed;
        const w3class = "w3-display-middle w3-display-container w3-button w3-half w3-center";
        if (!displayed) {
            toastClass = "hide";
        }
        else {
            if(value.level == LEVEL.INFO) {
                    toastClass = w3class+" info ";
                }
                else if(value.level == LEVEL.WARN) {
                    toastClass = w3class+" warn";
                }
                else if(value.level == LEVEL.ERROR) {
                    toastClass = w3class+" error";
                }
                else {
                    toastClass = "info";
                }
            }     
        });
    

    onDestroy(unsubscribe);
    
    function hideToast() {
        toastClass = "hide";
        displayed = false;
        message = "";
    }

</script>
<div class="w3-display-container">
<div class={toastClass} on:click={() => {hideToast()}}>
    <span on:click={() => {hideToast()}}>{@html message}</span>
    <span on:click={() => {hideToast()}} class="w3-button w3-display-right">&times;</span>
</div>
</div>