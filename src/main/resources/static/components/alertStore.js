import { writable } from 'svelte/store';

export const LEVEL = {
    INFO: "info",
    WARN: "warn",
    ERROR: "error"
}

export const alertMessage = writable({
    message:"",
    displayed:false,
    level:LEVEL.INFO
});

export function alert(level, message) {
    alertMessage.update(alertMess => { return {
        "level" : level,
        "message" : message,
        "displayed" : true
        }
        });
}

export function alertInfo(message) {
   alert(LEVEL.INFO,message);
}

export function alertWarn(message) {
    alert(LEVEL.WARN,message);
 }

 export function alertError(message) {
    alert(LEVEL.ERROR,message);
 }
