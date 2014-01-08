package bid.api.rest.tech;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class BidApplication extends ResourceConfig {

    public BidApplication() {
        register(new BidBinder());
        ResourceConfig resourceConfig = packages("bid.api.rest");
    }
}
