package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ListenableFuture2<E> extends Future<E> {
    void addListener(Runnable r, Executor e);
}