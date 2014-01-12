package bid.api.rest.jersey;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class BidApplication extends ResourceConfig {
    public BidApplication() {
        register(new BidBinder()).register(JacksonFeature.class);
        packages("bid.api.rest", "bid.api.rs", "bid.api.ws");
    }
}
