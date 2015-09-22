package org.haldokan.edge.lunchbox.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by haytham.aldokanji on 9/21/15.
 */
public class Brand {
    private long id;
    @Length(max = 15)
    private String content;

    public Brand() {
    }

    public Brand(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}
