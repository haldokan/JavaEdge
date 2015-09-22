package org.haldokan.edge.lunchbox.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by haytham.aldokanji on 9/21/15.
 */
public class AutomakerApp extends Application<AutomakerConfig> {

    public static void main(String[] args) throws Exception {
        new AutomakerApp().run(args);
    }

    @Override
    public String getName() {
        return "Automaker";
    }

    @Override
    public void initialize(Bootstrap<AutomakerConfig> bootstrap) {

    }

    @Override
    public void run(AutomakerConfig automakerConfig, Environment environment) throws Exception {
        AutomakerResource resource = new AutomakerResource(automakerConfig.getTemplate(), automakerConfig.getDefaultBrand());
        environment.jersey().register(resource);
        TemplateHealthCheck healthCheck = new TemplateHealthCheck(automakerConfig.getTemplate());
        environment.healthChecks().register("template", healthCheck);
    }
}
