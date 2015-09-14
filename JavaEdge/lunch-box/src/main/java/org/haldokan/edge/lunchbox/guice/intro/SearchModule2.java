package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.AbstractModule;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class SearchModule2 extends AbstractModule {
    @Override
    protected void configure() {
        bind(SearchDomain.class).to(Image.class);
        bind(SearchService.class).to(YahooSearch.class);
    }
}
