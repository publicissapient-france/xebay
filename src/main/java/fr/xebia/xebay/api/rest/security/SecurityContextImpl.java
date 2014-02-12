package fr.xebia.xebay.api.rest.security;

import fr.xebia.xebay.domain.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    private final User user;

    public SecurityContextImpl(User user) {
        this.user = user;
    }

    public SecurityContextImpl(String key, String name) {
        this.user = new User(key, name);
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
        return false;
    }
}