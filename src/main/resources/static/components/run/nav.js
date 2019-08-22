import { createEventDispatcher } from "svelte";



// export function move(dispatch, name, label, path, id) {
//     //const dispatch = createEventDispatcher();
//   dispatch("move", {
//     "name": name,
//     "label": label,
//     "path": path,
//     "id": id
//   });

  export function mover(dispatch) {
      return function(name, label, path, id) {
        //const dispatch = createEventDispatcher();
      dispatch("move", {
        "name": name,
        "label": label,
        "path": path,
        "id": id
      });
    }
  }
