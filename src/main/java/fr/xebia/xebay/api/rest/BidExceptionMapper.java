package fr.xebia.xebay.api.rest;

import fr.xebia.xebay.domain.BidException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BidExceptionMapper implements ExceptionMapper<BidException> {
    @Override
    public Response toResponse(BidException e) {
            Response response = Response.status(Status.FORBIDDEN.getStatusCode()).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        return response;
        }
    }