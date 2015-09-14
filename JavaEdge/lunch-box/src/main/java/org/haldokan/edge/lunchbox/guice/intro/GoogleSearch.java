package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.Singleton;

import javax.inject.Inject;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
@Singleton
public class GoogleSearch implements SearchService {
    private SearchDomain searchDomain;

    @Inject
    public GoogleSearch(SearchDomain searchDomain) {
        this.searchDomain = searchDomain;
    }

    @Override
    public String search(String query) {
        return "Google search for '" + query + "' using " + searchDomain.getFeatures();
    }
}
