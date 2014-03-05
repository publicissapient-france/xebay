package fr.xebia.xebay.api.rest.security;

import fr.xebia.xebay.domain.internal.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {
    private final User user;

    public SecurityContextImpl(User user) {
        this.user = user;
    }

    @Override
    public String getAuthenticationScheme() {
        // This is not a basic authentication because there is no user:password base64 encoded into Authorization header
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
        return user.isInRole(role);
    }
}