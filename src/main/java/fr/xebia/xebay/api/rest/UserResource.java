package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.api.rest.security.UserAuthorization;
import fr.xebia.xebay.domain.Users;
import fr.xebia.xebay.domain.model.PublicUser;
import fr.xebia.xebay.domain.model.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.Set;

import static fr.xebia.xebay.BidServer.BID_SERVER;
import static fr.xebia.xebay.domain.AdminUser.ADMIN_ROLE;

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

    @GET
    @Path("/publicUsers")
    public Set<PublicUser> getUsers() {
        return users.getUsers();
    }

    @GET
    @UserAuthorization
    @RolesAllowed(ADMIN_ROLE)
    public Set<User> getUserSet() {
        // Lambdas won't work here dude... let me know if there is a workaround with lambdas, it should be as simple as
        // return users.getUserSet().stream().map(fr.xebia.xebay.domain.User::toUser).collect(Collectors.toSet())
        Set<User> mappedUserSet = new HashSet<>();
        Set<fr.xebia.xebay.domain.User> userSet = users.getUserSet();
        for (fr.xebia.xebay.domain.User user : userSet) {
            mappedUserSet.add(user.toUser());
        }
        return mappedUserSet;
    }

    @GET
    @Path("/info")
    @UserAuthorization
    public User getUser(@Context SecurityContext securityContext) {
        return ((fr.xebia.xebay.domain.User) securityContext.getUserPrincipal()).toUser();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    @UserAuthorization
    @RolesAllowed(ADMIN_ROLE)
    public String register(@QueryParam("name") String name) {
        fr.xebia.xebay.domain.User user = users.create(name);
        return user.getKey();
    }

    @DELETE
    @Path("/unregister")
    @UserAuthorization
    @RolesAllowed(ADMIN_ROLE)
    public void unregister(@QueryParam("key") String key) {
        fr.xebia.xebay.domain.User user = users.remove(key);
        BID_SERVER.bidEngine.userIsUnregistered(user);
    }
}
