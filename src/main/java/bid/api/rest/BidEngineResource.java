package bid.api.rest;

import bid.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bidEngine")
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;

    public BidEngineResource() {
        bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
    }

    @GET
    @Path("/register")
    public String register(@QueryParam("email") String email){
        try {
            return bidEngine.register(email);
        } catch (BidException e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .type("text/plain")
                    .build());
        }
    }

    @GET
    @Path("/unregister/{email}")
    public void unregister(@PathParam("email") String email, @QueryParam("key") String key){
        try {
            bidEngine.unregister(key, email);
        } catch (BidException e) {
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .type("text/plain")
                    .build());
        }
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
