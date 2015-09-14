package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.AbstractModule;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class SearchModule1 extends AbstractModule {
    @Override
    protected void configure() {
        bind(SearchDomain.class).to(Web.class);
        bind(SearchService.class).to(GoogleSearch.class);
    }
}
