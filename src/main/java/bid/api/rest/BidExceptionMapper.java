package bid.api.rest;

import bid.BidException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BidExceptionMapper implements ExceptionMapper<BidException> {

    @Override
    public Response toResponse(BidException e) {
        // Renvoyer un statut 400 lors de la levée d'une BidException
        return Response.status(Status.FORBIDDEN).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
    }
}
