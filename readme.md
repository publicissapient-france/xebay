## Bid

### How to start server

    $ mvn tomcat7:run

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - provide a mechanism to create bid offer by server or by clients
 - a bid offer should provide a start price different from real item value
 - web : implement a web client to bid with buttons and forms.
 - a bid has predefined length : each response has to contains the number of milliseconds until end of current bid
 - web apis has to be as same as possible between rest and web socket
 - web : as a developer, I should register myself with an email and get an api key
 - web : as a visitor, I should see graph of values of each items
