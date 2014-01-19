package bid.api.rest;

import bid.api.rest.dto.UserInfo;
import bid.api.rest.security.UserAuthorization;
import bid.domain.User;
import bid.domain.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private Users users;

    @Inject
    public UserResource(Users users) {
        this.users = users;
    }


    User getUser(@PathParam("key") String key) {
            return users.getUser(key);
    }

    @GET
    @Path("/info")
    @UserAuthorization
    public UserInfo getUserInfo(@QueryParam("email") String email, @Context SecurityContext securityContext) {
        User user = (User)securityContext.getUserPrincipal();
        return UserInfo.newUserInfo(user);
    }


    @GET
    @Path("/register")
    public String register(@QueryParam("email") String email){
            User user = users.create(email);
            return user.getKey();
    }

    //@RolesAllowed("admin")
    @GET
    @Path("/unregister")
    public void unregister(@QueryParam("email") String email, @QueryParam("key") String key){
        try {
            users.remove(key, email);
        } catch (Exception e) { //TODO BidException(403) ou NotAllowedException (401)
            throw new WebApplicationException(
                    Response.status(Response.Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .type("text/plain")
                    .build());
        }
    }
}
