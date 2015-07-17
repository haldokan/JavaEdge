package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface ExecutorService2 {
    <E> ListenableFuture2<E> submit(Callable<E> c);

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    void shutdown();
}
