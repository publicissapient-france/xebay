package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.BidOfferInfo;
import fr.xebia.xebay.api.rest.dto.BidParam;
import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.User;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@Singleton
@Path("/bidEngine")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    @GET
    @Path("/")
    public BidOfferInfo currentBidOffer() {
        return BidOfferInfo.newBidOfferInfo(BID_SERVER.bidEngine.currentBidOffer());
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UserAuthorization
    public BidOfferInfo bid(@FormParam("name") String name, @FormParam("value") Double curValue, @FormParam("increment") Double increment, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return BidOfferInfo.newBidOfferInfo(BID_SERVER.bidEngine.bid(user, name, curValue, increment));
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public BidOfferInfo bid(BidParam bidParam, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return BidOfferInfo.newBidOfferInfo(BID_SERVER.bidEngine.bid(user, bidParam.getItemName(), bidParam.getCurValue(), bidParam.getIncrement()));
    }
}
