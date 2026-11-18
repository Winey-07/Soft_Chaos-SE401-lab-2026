package edu.itc.shop_rest.api.filter;

import java.util.UUID;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
public class CorrelationFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final String HEADER = "X-Correlation-Id";
    private static final String START_PROP = "req.start";
    private static final System.Logger LOG = System.getLogger("api.access");

    @Override
    public void filter(ContainerRequestContext req) {
        req.setProperty(START_PROP, System.nanoTime());
        String id = req.getHeaderString(HEADER);
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        req.setProperty(HEADER, id);
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        String id = (String) req.getProperty(HEADER);
        if (id != null) {
            res.getHeaders().putSingle(HEADER, id);
        }
        Long start = (Long) req.getProperty(START_PROP);
        long ms = (start != null) ? (System.nanoTime() - start) / 1_000_000L : 0L;
        LOG.log(System.Logger.Level.INFO, "{0} /{1} -> {2} [{3}] {4} ms",
                req.getMethod(), req.getUriInfo().getPath(), res.getStatus(), id, ms);
    }
}