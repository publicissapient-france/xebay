package bid.api.rest;

import bid.*;
import bid.api.rest.security.UserAuthorization;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/bidEngine")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;


    private Users users;

    @Inject
    public BidEngineResource(Users users) {
        bidEngine = new BidEngine(new Items(new Item("an item", 4.3)));
    }

    @GET
    @Path("/")
    public BidOffer currentBidOffer() {
        return bidEngine.currentBidOffer();
    }


    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UserAuthorization
    public BidOffer bid(@FormParam("name") String name, @FormParam("value") Double curValue, @FormParam("increment") Double increment, @Context SecurityContext securityContext){
        User user = (User)securityContext.getUserPrincipal();
        return bidEngine.bid(user, name, curValue, increment);
    }


    @POST
    @Path("/bid/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public BidOffer bid(@PathParam("id") String id,
                        BidOfferParam bidOfferParam,
                        @Context SecurityContext securityContext){
        User user = (User)securityContext.getUserPrincipal();
        return bidEngine.bid(user, bidOfferParam.getName(), bidOfferParam.getCurValue(), bidOfferParam.getIncrement());
    }

}
