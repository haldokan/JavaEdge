package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.AbstractModule;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class SearchModule1 extends AbstractModule {
    @Override
    protected void configure() {
        // in order to map a type to more than one implementation we have to use binding annotations: the combination of
        // the type and binding annotation forms a key for the binding
        bind(SearchDomain.class).annotatedWith(WebDomain.WebBinding.class).to(WebDomain.class);
        bind(SearchDomain.class).annotatedWith(ImageDomain.ImageBinding.class).to(WebDomain.class);
        bind(SearchService.class).annotatedWith(GoogleSearch.GoogleSearchBinding.class).
                to(GoogleSearch.class);
        bind(SearchService.class).annotatedWith(YahooSearch.YahooSearchBinding.class).
                to(YahooSearch.class);

        // when creating objects become complex we use providers.
    }
}
