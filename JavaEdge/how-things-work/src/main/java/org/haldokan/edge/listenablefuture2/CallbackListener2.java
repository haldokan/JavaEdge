package org.haldokan.edge.listenablefuture2;


public interface CallbackListener2<E> {
    void onSuccess(E e);

    void onFailure(Throwable t);
}
