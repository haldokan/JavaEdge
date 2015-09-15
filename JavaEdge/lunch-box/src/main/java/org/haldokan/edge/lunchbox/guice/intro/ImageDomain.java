package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class ImageDomain implements SearchDomain {
    @Override
    public String getFeatures() {
        return "IMAGE Search Features";
    }

    ;

    @BindingAnnotation
    @Target({FIELD, METHOD, PARAMETER})
    @Retention(RUNTIME)
    public @interface ImageBinding {
    }
}
