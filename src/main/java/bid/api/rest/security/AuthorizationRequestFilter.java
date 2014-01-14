package bid.api.rest.security;

import bid.User;
import bid.Users;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

@Priority(Priorities.AUTHORIZATION)
@UserAuthorization
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private static final Logger log = Logger.getLogger("AuthorizationRequestFilter");

    @Inject
    private Users users;

    @Override
    public void filter(ContainerRequestContext request)
            throws IOException {

        String authToken = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        User user = authorize(authToken, request.getMethod(), request.getDate());
        request.setSecurityContext(new SecurityContextImpl(user));
    }

    private User authorize(String authToken, String method, Date date){
        if (null == authToken) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("User-Key header must be defined.")
                    .build());
        }


        User user = users.getUser(authToken);
        if (null == user) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User not authenticated")
                    .build());
        }

        return user;
    }
}