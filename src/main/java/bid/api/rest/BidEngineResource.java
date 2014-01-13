package bid.api.rest;

import bid.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/bidEngine")
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;


    private Users users;

    @Inject
    public BidEngineResource(Users users) {
        bidEngine = new BidEngine(new Items(new Item("an item", 4.3)), users);
    }

    @GET
    @Path("/")
    public BidOffer currentBidOffer() {
        return bidEngine.currentBidOffer();
    }


    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public BidOffer bid(@FormParam("name") String name, @FormParam("value") Double curValue, @FormParam("increment") Double increment, @FormParam("key") String key){
        return bidEngine.bid(key, name, curValue, increment);
    }


    @POST
    @Path("/bid/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public BidOffer bid(@PathParam("id") String id,
                        BidOfferParam bidOfferParam,
                        @HeaderParam("key") String key){
        //todo
        return bidEngine.bid(key, bidOfferParam.getName(), bidOfferParam.getCurValue(), bidOfferParam.getIncrement());
    }

}
