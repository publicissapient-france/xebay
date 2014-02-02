package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.BidOfferInfo;
import fr.xebia.xebay.api.rest.dto.BidParam;
import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.User;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static fr.xebia.xebay.BidServer.BID_SERVER;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
@Path("/bidEngine")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;

    public BidEngineResource() {
        this.bidEngine = BID_SERVER.bidEngine;
    }

    BidEngineResource(BidEngine bidEngine) {
        this.bidEngine = bidEngine;
    }

    @GET
    @Path("/current")
    public BidOfferInfo currentBidOffer() {
        BidOffer currentBidOffer = bidEngine.currentBidOffer();
        if (currentBidOffer == null) {
            throw new WebApplicationException(NOT_FOUND);
        }
        return BidOfferInfo.newBidOfferInfo(currentBidOffer);
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UserAuthorization
    public BidOfferInfo bid(@FormParam("name") String name, @FormParam("value") Double curValue, @FormParam("increment") Double increment, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return BidOfferInfo.newBidOfferInfo(bidEngine.bid(user, name, curValue, increment));
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public BidOfferInfo bid(BidParam bidParam, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return BidOfferInfo.newBidOfferInfo(bidEngine.bid(user, bidParam.getItemName(), bidParam.getCurValue(), bidParam.getIncrement()));
    }
}
