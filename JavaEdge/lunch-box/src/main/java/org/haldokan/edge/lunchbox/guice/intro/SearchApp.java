package org.haldokan.edge.lunchbox.guice.intro;

import javax.inject.Inject;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class SearchApp {
    private final SearchService messageService;

    @Inject
    public SearchApp(SearchService messageService) {
        this.messageService = messageService;
    }

    public String search(String msg) {
        return messageService.search(msg);
    }
}
