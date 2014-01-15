package bid.api.rest;

import bid.*;
import bid.api.rest.security.UserAuthorization;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

@Path("/bidEngine")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private static final Logger log = Logger.getLogger("BidEngineResource");

    private final BidEngine bidEngine;

    public BidEngineResource() {
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
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public BidOffer bid(
                        BidParam bidParam,
                        @Context SecurityContext securityContext){
        User user = (User)securityContext.getUserPrincipal();
        return bidEngine.bid(user, bidParam.getItemName(), bidParam.getCurValue(), bidParam.getIncrement());
    }

}
