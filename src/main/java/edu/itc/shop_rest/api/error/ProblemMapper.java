package edu.itc.shop_rest.api.error;

import java.util.UUID;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import edu.itc.shop_rest.api.dto.Problem;

@Provider
public class ProblemMapper implements ExceptionMapper<Throwable> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof WebApplicationException wae) {
            int status = wae.getResponse().getStatus();
            String title = wae.getResponse().getStatusInfo().getReasonPhrase();
            String detail = (wae.getCause() instanceof IllegalArgumentException iae)
                    ? iae.getMessage()
                    : wae.getMessage();
            var problem = new Problem(
                    "about:blank",
                    title,
                    status,
                    detail,
                    uriInfo.getRequestUri().getPath(),
                    null
            );
            return Response.status(status).type(Problem.MEDIA_TYPE).entity(problem).build();
        }

        String ref = UUID.randomUUID().toString();
        System.getLogger("api").log(System.Logger.Level.ERROR, "Unhandled error [" + ref + "]", ex);
        var problem = new Problem(
                "about:blank",
                "Internal Server Error",
                500,
                "reference " + ref,
                uriInfo.getRequestUri().getPath(),
                null
        );
        return Response.status(500).type(Problem.MEDIA_TYPE).entity(problem).build();
    }
}