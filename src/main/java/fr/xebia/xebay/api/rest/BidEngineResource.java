package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.BidDemand;
import fr.xebia.xebay.api.rest.jersey.PATCH;
import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.BidEngine;
import fr.xebia.xebay.domain.BidOffer;
import fr.xebia.xebay.domain.internal.Item;
import fr.xebia.xebay.domain.internal.Items;
import fr.xebia.xebay.domain.internal.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static fr.xebia.xebay.BidServer.BID_SERVER;
import static fr.xebia.xebay.domain.internal.AdminUser.ADMIN_ROLE;
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
    public BidOffer bid(@FormParam("name") String itemName, @FormParam("value") Double newValue, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return bidEngine.bid(user, itemName, newValue);
    }

    @POST
    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public BidOffer bid(BidDemand bidDemand, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return bidEngine.bid(user, bidDemand.getItemName(), bidDemand.getValue());
    }

    @POST
    @Path("/offer")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserAuthorization
    public Response offer(BidDemand bidDemand, @Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        Item item = items.find(bidDemand.getItemName());
        if (null == item) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("item \"%s\" doesn't exist", bidDemand.getItemName()))
                    .build());
        }
        bidEngine.offer(user, item, bidDemand.getValue());
        return Response.ok().build();
    }

    @PATCH
    @Path("/plugin/{name}")
    @UserAuthorization
    @RolesAllowed(ADMIN_ROLE)
    public void status(@PathParam("name") String name, @QueryParam("active") boolean active) {
        if (active) {
            bidEngine.activate(name);
        } else {
            bidEngine.deactivate(name);
        }
    }
}
