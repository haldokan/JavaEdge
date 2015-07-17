package org.haldokan.edge.listenablefuture2;

import java.util.concurrent.ExecutorService;

public class Executors2 {
    public static ExecutorService2 listeningDecorator(ExecutorService es) {
	return new ExecutorServiceInternal2(es);
    }
}
