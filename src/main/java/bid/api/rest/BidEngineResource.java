package bid.api.rest;

import bid.*;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/bidEngine")
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {

    private final BidEngine bidEngine;

    public BidEngineResource() {
        bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
    }

    @GET
    @Path("/register")
    public String register(@QueryParam("email") String email) throws BidException {
        return bidEngine.register(email);
    }

    @GET
    @Path("/unregister/{email}")
    public void unregister(@PathParam("email") String email, @QueryParam("key") String key) throws BidException {
        bidEngine.unregister(key, email);
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
    public BidOffer bid(@PathParam("name") String name,
               @QueryParam("key") String key,
               @QueryParam("value") Double value,
               @QueryParam("increment") Double increment){
        return bidEngine.bid(key, name, value, increment);
    }

}
