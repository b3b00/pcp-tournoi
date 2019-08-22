> [run](#/run(X)) > [groups](#/run(X)/groups(Y)) > [group](#/run(X)/groups(Y)/group(Z)) 

> [run](#/run(X)) > [tableau A](#/run(X)/table('A')) > [group](#/run(X)/groups(Y)/group(Z)) 


```
 <Bread items={} on:click={}>
```

 ### items :
 liste d'objets :

```
 {

     name : "nom du component",
     label : "label du lien dans le fil",
     path : "path",
     id : "id optionnel de la resource " 
 }
 ```
 
 on itére pour construire le fil d'ariane :
  * 1 item = 1 niveau
  * lien : concat des path

1 path d'élément =
```
itemPath = path + (item.id != null) ? `(${item.id})` : "";
```

### on:click

callback avec comme payload les items jusqu'à celui qui a été choisi

=> run.svelte contient un gros switch case qui lui permet de choisir la bon component à afficher.



# run.svelte

est un composant "layout" :

```html

<script>

    let currentBreadCrumb;

    function breadCrumbCallback(data) {
        /*
        breadcrumb = data.detail.bread;
            initialise toutes les 
            variables appropriées(
             tournament,groups, group, round,...) en fonction du contenu de breadcrumb

             currentcrumb = breadcrumb;
        */
    }
</script>

<BreadCrumb items on:click>

{#if currentCrumb.last.name == "groups"}
{:else if currentCrumb.last.name == "groups"}
    <Groups groups={groups}>
{:else if currentCrumb.last.name == "groups"}
    <Group group={group} tournament={tournament}>
{:else}
   // racine : run, rien à afficher
{/if}
```

## comment gérer les liens d'avancée ?

normalement juste un push d'item dans run doit faire le job si on vient ensuite modifier la propriété currentBreadCrumb.

chaque component doit pouvoir lever un dispatch "moveCrumb" trappé par run.svelte


# matchs
les matchs s'ouvrent dans des dialog modales (se sont les derniers éléments).