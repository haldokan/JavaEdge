package org.haldokan.edge.lunchbox.dropwizard;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by haytham.aldokanji on 9/21/15.
 */
public class TemplateHealthCheck extends HealthCheck {
    private final String template;

    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        String result = String.format(template, "DUMMY");
        if (!result.contains("DUMMY")) {
            return Result.unhealthy("Could not format DUMMY! Something terribly wrong");
        }
        return Result.healthy();
    }
}
