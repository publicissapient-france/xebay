package bid.api.rest;

import bid.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/bidEngine")
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;

    @Inject
    Users users;

    public BidEngineResource() {
        bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
    }

    @GET
    @Path("/")
    public BidOffer currentBidOffer() {
        return bidEngine.currentBidOffer();
    }


    @GET
    @Path("/{name}")
    public BidOffer getBidOffer(@PathParam("name") String name) {
        return bidEngine.getBidOffer(name);
    }


    @POST
    @Path("/{name}")
    BidOffer bid(@PathParam("name") String name,
               @QueryParam("key") String key,
               @QueryParam("value") Double value,
               @QueryParam("increment") Double increment){
        return bidEngine.bid(key, name, value, increment);
    }

}
