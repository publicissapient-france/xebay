package bid.api.rest;

import bid.User;
import bid.Users;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private Users users;

    @Inject
    public UserResource(Users users) {
        this.users = users;
    }


    @GET
    @Path("/{key}")
    public User getUser(@PathParam("key") String key) {
            return users.getUser(key);
    }



    @GET
    @Path("/register")
    public String register(@QueryParam("email") String email){
            User user = users.create(email);
            return user.getKey();
    }

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
