package org.haldokan.edge.guava;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuavaTimeLimiter implements IGuavaTimeLimiter {
    public static void main(String[] args) throws Exception {
        TimeLimiter tl = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());
        IGuavaTimeLimiter proxy = tl.newProxy(new GuavaTimeLimiter(), IGuavaTimeLimiter.class, 100,
                TimeUnit.MILLISECONDS);
        try {
            // not that if we switch the order of these 2 lines only the 200L will be printed
            System.out.println(proxy.m("foobar", 50L));
            System.out.println(proxy.m("foobar", 200L));
        } catch (UncheckedTimeoutException e) {
            System.out.println("defalut");
        }
    }

    @Override
    public String m(String s, long stime) throws InterruptedException {
        Thread.sleep(stime);
        return s.toUpperCase();
    }
}
