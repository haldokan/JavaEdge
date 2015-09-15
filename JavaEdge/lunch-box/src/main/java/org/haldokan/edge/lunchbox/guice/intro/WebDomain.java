package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.BindingAnnotation;

import javax.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class WebDomain implements SearchDomain {
    private final DomainGraph domainGraph;

    @Inject
    public WebDomain(DomainGraph domainGraph) {
        this.domainGraph = domainGraph;
    }

    @Override
    public String getFeatures() {
        return "WEB Search Features";
    }

    ;

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface WebBinding {
    }
}
