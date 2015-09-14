package org.haldokan.edge.lunchbox.guice.intro;

import javax.inject.Inject;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class YahooSearch implements SearchService {
    private SearchDomain searchDomain;

    @Inject
    public YahooSearch(SearchDomain searchDomain) {
        this.searchDomain = searchDomain;
    }

    @Override
    public String search(String query) {
        return "Yahoo search for '" + query + "' using " + searchDomain.getFeatures();
    }
}
