package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.api.rest.dto.ItemOffer;
import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.*;

import javax.annotation.security.PermitAll;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static fr.xebia.xebay.BidServer.BID_SERVER;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
@Path("/bidEngine")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class BidEngineResource {
    private final BidEngine bidEngine;
    private final Items items;

    public BidEngineResource() {
        this.bidEngine = BID_SERVER.bidEngine;
        this.items = BID_SERVER.items;
    }

    BidEngineResource(BidEngine bidEngine, Items items) {
        this.bidEngine = bidEngine;
        this.items = items;
    }

    @GET
    @Path("/current")
    public BidOffer currentBidOffer() {
        BidOffer currentBidOffer = bidEngine.currentBidOffer();
        if (currentBidOffer == null) {
            throw new WebApplicationException(NOT_FOUND);
        }
        return currentBidOffer;
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @UserAuthorization
    public BidOffer bid(@FormParam("name") String name, @FormParam("value") Double curValue, @FormParam("increment") Double increment, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return bidEngine.bid(user, name, curValue, increment);
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public BidOffer bid(BidDemand bidDemand, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return bidEngine.bid(user, bidDemand.getItemName(), bidDemand.getCurrentValue(), bidDemand.getIncrement());
    }

    @POST
    @Path("/offer")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public void offer(ItemOffer itemOffer, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        Item item = items.find(itemOffer.getName());
        if(null == item){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("item \"%s\" doesn't exist", itemOffer.getName()))
                    .build());
        }
        bidEngine.offer(item, itemOffer.getValue(), user);
    }

}
