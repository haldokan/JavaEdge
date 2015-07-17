package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class Futures2 {
	public static <E> void addCallback(ListenableFuture2<E> lf,
			CallbackListener2<E> cbl, Executor e) {
		lf.addListener(new Runnable() {
			@Override
			public void run() {
				try {
					cbl.onSuccess(lf.get());
				} catch (InterruptedException | ExecutionException e) {
					cbl.onFailure(e);
				}
			}
		}, e);
	}
}