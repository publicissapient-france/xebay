package fr.xebia.xebay.api.rest.jersey;

import fr.xebia.xebay.api.rest.security.AuthorizationRequestFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class BidApplication extends ResourceConfig {
    public BidApplication() {
        register(new BidBinder()).register(JacksonFeature.class)
                                 .register(RolesAllowedDynamicFeature.class)
                                 .register(AuthorizationRequestFilter.class);

        packages("fr.xebia.xebay.api.rest", "fr.xebia.xebay.api.rs", "fr.xebia.xebay.api.ws");
    }
}
