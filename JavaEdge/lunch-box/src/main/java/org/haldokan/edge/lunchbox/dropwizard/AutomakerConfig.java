package org.haldokan.edge.lunchbox.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by haytham.aldokanji on 9/21/15.
 */
public class AutomakerConfig extends Configuration {
    @NotEmpty
    private String template;
    @NotEmpty
    private String defaultBrand;

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultBrand() {
        return defaultBrand;
    }

    @JsonProperty
    public void setDefaultBrand(String defaultBrand) {
        this.defaultBrand = defaultBrand;
    }
}
