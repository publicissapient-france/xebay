package bid.api.rest.security;

import bid.domain.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    private final User user;

    public SecurityContextImpl(User user) {
        this.user = user;
    }
    public SecurityContextImpl(String key, String userEmail) {
        this.user = new User(key, userEmail);
    }


    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isUserInRole(String role) {

            // Forbidden
//            Response denied = Response.status(Response.Status.FORBIDDEN).entity("Permission Denied").build();
//            throw new WebApplicationException(denied);


        // this user has this role?
        //return user.getRoles().contains(User.Role.valueOf(role));
        return false;
    }
}