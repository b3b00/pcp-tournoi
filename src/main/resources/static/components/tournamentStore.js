import { writable } from 'svelte/store';


export const tournament = writable({
   
});

export function setGlobalTournament(newTournament) {
    tournament.update(r => { return newTournament
        });
}

