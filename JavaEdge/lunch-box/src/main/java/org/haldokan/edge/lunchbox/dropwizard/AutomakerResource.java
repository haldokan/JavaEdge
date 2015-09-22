package org.haldokan.edge.lunchbox.dropwizard;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by haytham.aldokanji on 9/21/15.
 */
@Path("/automaker")
@Produces(MediaType.APPLICATION_JSON)
public class AutomakerResource {
    private final String template;
    private final String defaultBrand;
    private final AtomicLong responseId;

    public AutomakerResource(String template, String defaultBrand) {
        this.template = template;
        this.defaultBrand = defaultBrand;
        this.responseId = new AtomicLong();
    }

    // the @Timed annotation tells Dropwizar to record as metrics the method call rate and duration
    @GET
    @Timed
    public Brand fetchBrand(@QueryParam("brand") Optional<String> brand) {
        String result = String.format(template, brand.or(defaultBrand));
        return new Brand(responseId.incrementAndGet(), result);
    }
}
