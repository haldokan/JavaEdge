package org.haldokan.edge.executorservice2;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ExecutorService2<V> {
    Future<V> submit(Callable<V> callable);
}
