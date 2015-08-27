package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * A listenable future needs a listener. We do that using api similar to that of Guava's.
 *
 * @author haldokan
 */
public class Futures2 {
    public static <E> void addCallback(ListenableFuture2<E> lf, CallbackListener2<E> cbl, Executor e) {
        lf.addListener(() -> {
            try {
                cbl.onSuccess(lf.get());
            } catch (InterruptedException | ExecutionException ee) {
                cbl.onFailure(ee);
            }
        }, e);
    }
}