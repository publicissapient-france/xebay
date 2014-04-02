## Bid

### How to start server

    $ mvn tomcat7:run

### Coding Style

You can use [editorconfig](http://editorconfig.org) to synchronize this project coding style.

### TODO

 - add client with main (branch)
 - web : as a visitor, I should see graph of values of each items
 - persist users (name, key)
 - an owner can bid on its own item, OK, but when no other user buys the item, item value is still increased, OK, but user balance is not decreased, KO

- @deprecated rename BidOfferToSell => ItemOffer, BidDemand => BidOffer and BidOffer to OfferResponse (or any better idea because I think names are not relevant) ?
- Maybe renames should look like this instead
- POST /bid     Bid => BidInfo (response with status OK, IN_PROGRESS, KO)
- POST /sale    Sale => SaleInfo (response with status OK, IN_PROGRESS, KO)
- GET /news     () => News (response)

nice to have :

 - web : provide a user page to update informations (name, avatar, etc.)
 - game : how much items / how much "value" => n° de collection
