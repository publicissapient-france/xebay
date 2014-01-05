package bid.api.rest;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class BidApplication extends ResourceConfig {

    public BidApplication() {
        ResourceConfig resourceConfig = packages("bid.api.rest");
    }
}
