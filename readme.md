## Bid

### How to start server

    $ mvn tomcat7:run

### How to add users (because there is no specific page)

    $ curl --header Authorization:4dm1n http://localhost:8080/rest/users/register?name=<name>

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - dev : unify input and output types for apis (immutable views of state of BidEngine when requested)
 - web : provide an admin page to add or remove users
 - web : provide a user page to update informations (name, avatar, etc.)
 - api : developer wants to sell items that he owns
 - web : as a visitor, I should see graph of values of each items
 - clients : implements selling items that are owned by a developper
