## Bid

### How to start server

    $ mvn tomcat7:run

### How to add users (because there is no specific page)

    $ curl --header Authorization:4dm1n http://localhost:8080/rest/users/register?name=<name>

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - engine : it seems that every item is resolved even though no user has bid on it => must check bidEngineListener calls (see browser logs)
 - web : as an owner I can sell my item at a choosen sell price
 - web : provide a user page to update informations (name, avatar, etc.)
 - web : as a visitor, I should see graph of values of each items
 - web : as a user i should see my collection changes as I acquire items (needs a browser refresh at the moment)
 - all : toggle custom rules to change the game
    - if a user owns every item of a category he gain a 500$ bonus
    - for next 5 bids bank gives 100$ to buyer
    - etc.
 - publish news with "active rules"