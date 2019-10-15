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