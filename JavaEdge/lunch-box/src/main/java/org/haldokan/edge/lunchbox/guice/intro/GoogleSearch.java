package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.BindingAnnotation;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
@Singleton
public class GoogleSearch implements SearchService {
    private SearchDomain searchDomain;

    ;

    @Inject
    public GoogleSearch(@WebDomain.WebBinding SearchDomain searchDomain) {
        this.searchDomain = searchDomain;
    }

    @Override
    public String search(String query) {
        return "Google search for '" + query + "' using " + searchDomain.getFeatures();
    }

    @BindingAnnotation
    @Target({FIELD, PARAMETER, METHOD})
    @Retention(RUNTIME)
    public @interface GoogleSearchBinding {
    }
}
