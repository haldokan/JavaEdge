package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The implementation of the Executor returns our own ListenableFuture2 instead of plain JDK Future
 *
 * @author haldokan
 */
public class ExecutorServiceInternal2 implements ExecutorService2 {
    private final ExecutorService es;

    public ExecutorServiceInternal2(ExecutorService es) {
        this.es = es;
    }

    @Override
    public <E> ListenableFuture2<E> submit(Callable<E> c) {
        return new FutureTask2<>(es.submit(c));
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return es.awaitTermination(timeout, unit);
    }

    @Override
    public void shutdown() {
        es.shutdown();
    }

}
