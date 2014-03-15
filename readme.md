## Bid

### How to start server

    $ mvn tomcat7:run

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - engine : it seems that every item is resolved even though no user has bid on it => must check bidEngineListener calls (see browser logs)
 - web : here comes angular (...in progress ...almost there : bid & websocket are still missing !)
 - web : as an owner I can sell my item at a choosen sell price
 - web : provide a user page to update informations (name, avatar, etc.)
 - web : as a visitor, I should see graph of values of each items
 - web : as a user i should see my collection changes as I acquire items (needs a browser refresh at the moment)
 - publish news with "active rules"
 - convert to BigDecimal
 ...
 - maybe publish an API to list available plugins with status ... maybe