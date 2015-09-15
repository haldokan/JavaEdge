package org.haldokan.edge.lunchbox.guice.intro;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by haytham.aldokanji on 9/14/15.
 */
public class Main {
    public static void main(String[] args) {
        // note that we cannot bind the same interface to more than one class in the same injector
        Injector injector1 = Guice.createInjector(new SearchModule1());
        SearchApp app1 = injector1.getInstance(SearchApp.class);
        System.out.println(app1.search("Life is a precious thing to waste"));
    }
}
