package org.haldokan.edge.lunchbox.reactivex;

import rx.Observable;

/**
 * Created by haytham.aldokanji on 9/28/15.
 */
public class HelloWorld {
    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.sayHello("Haytham", "Rob", "Francisca");
    }

    public void sayHello(String... strings) {
        Observable.from(strings).subscribe(s -> {
            System.out.println("Hello " + s);
        });
    }
}
