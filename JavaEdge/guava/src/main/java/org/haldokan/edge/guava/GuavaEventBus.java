package org.haldokan.edge.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class GuavaEventBus {
    private static EventBus eb;

    public static void main(String[] args) {
        eb = new EventBus("eb");
        eb.register(new Sub1());
        eb.register(new Sub2());

        for (int i = 0; i < 10; i++) {
            eb.post(i);
            eb.post("s" + i);
        }

    }

    private static class Sub1 {
        @Subscribe
        public void sub(String s) throws InterruptedException {
            System.out.println("sub1->" + s);
            Thread.sleep(500);
        }
    }

    private static class Sub2 {
        @Subscribe
        @AllowConcurrentEvents
        public void sub(Integer i) {
            System.out.println("sub2->" + i);
        }
    }
}
