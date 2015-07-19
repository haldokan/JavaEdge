package org.haldokan.edge.executorservice2;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ExecutorService2<V> {
    //TODO implement executor shutdown
    Future<V> submit(Callable<V> callable);
}
