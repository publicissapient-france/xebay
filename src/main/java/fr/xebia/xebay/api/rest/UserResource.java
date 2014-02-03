package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.dto.UserInfo;
import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.User;
import fr.xebia.xebay.domain.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private Users users;

    public UserResource(Users users) {
        this.users = users;
    }

    public UserResource() {
        this.users = BID_SERVER.users;
    }

    User getUser(@PathParam("key") String key) {
        return users.getUser(key);
    }

    @GET
    @Path("/info")
    @UserAuthorization
    public UserInfo getUserInfo(@Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return UserInfo.newUserInfo(user);
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    public String register(@QueryParam("email") String email) {
        User user = users.create(email);
        return user.getKey();
    }

    @DELETE
    @Path("/unregister")
    @UserAuthorization
    public void unregister(@Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        users.remove(user.getKey(), user.getEmail());
    }
}
