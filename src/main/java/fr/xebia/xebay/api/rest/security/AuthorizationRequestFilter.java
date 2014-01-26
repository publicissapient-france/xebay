package fr.xebia.xebay.api.rest.security;

import fr.xebia.xebay.domain.User;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Logger;

import static fr.xebia.xebay.BidServer.BID_SERVER;

@Priority(Priorities.AUTHORIZATION)
@UserAuthorization
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private static final Logger log = Logger.getLogger("AuthorizationRequestFilter");

    @Override
    public void filter(ContainerRequestContext request)
            throws IOException {

        String authToken = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        log.info("user token " + authToken);
        User user = authorize(authToken);
        request.setSecurityContext(new SecurityContextImpl(user));
    }

    private User authorize(String authToken) {
        if (null == authToken) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("User-Key header must be defined.")
                    .build());
        }
        User user = BID_SERVER.users.getUser(authToken);
        if (null == user) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User not authenticated")
                    .build());
        }

        return user;
    }
}