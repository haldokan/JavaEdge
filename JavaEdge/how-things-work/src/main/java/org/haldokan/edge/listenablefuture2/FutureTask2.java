package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTask2<E> implements ListenableFuture2<E> {
	private Future<E> f;

	public FutureTask2(Future<E> f) {
		this.f = f;
	}

	@Override
	public void addListener(Runnable r, Executor e) {
		e.execute(r);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return f.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return f.isCancelled();
	}

	@Override
	public boolean isDone() {
		return f.isDone();
	}

	@Override
	public E get() throws InterruptedException, ExecutionException {
		return f.get();
	}

	@Override
	public E get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return f.get(timeout, unit);
	}
}
