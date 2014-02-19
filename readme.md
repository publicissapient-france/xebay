## Bid

### How to start server

    $ mvn tomcat7:run

### How to add users (because there is no specific page)

    $ curl --header Authorization:4dm1n http://localhost:8080/rest/users/register?name=<name>

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - web : provide an admin page to add or remove users
 - web : provide a user page to update informations (name, avatar, etc.)
 - web : as a visitor, I should see graph of values of each items
 - all : toggle custom rules to change the game
    - if a user sell an item at his sell price, bank buy it right now without any conditions
    - if a user owns every item of a category he gain a 500$ bonus
    - for next 5 bids bank gives 100$ to buyer
    - etc.