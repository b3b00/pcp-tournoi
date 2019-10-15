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
    background-color : blue;
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
        if (!displayed) {
            toastClass = "hide";
        }
        else {
            if(value.level == LEVEL.INFO) {
                    toastClass = "w3-container info ";
                }
                else if(value.level == LEVEL.WARN) {
                    toastClass = "w3-container warn";
                }
                else if(value.level == LEVEL.ERROR) {
                    toastClass = "w3-container error";
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

<div class={toastClass}>
    <span>{message}</span>
    <span on:click={() => {hideToast()}} class="w3-button w3-display-right">&times;</span>
</div>