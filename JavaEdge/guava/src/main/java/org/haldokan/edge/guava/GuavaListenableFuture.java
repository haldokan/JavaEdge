package org.haldokan.edge.guava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class GuavaListenableFuture {
	private static Random rand = new Random();

	public static void main(String[] args) throws Exception {
		ListeningExecutorService les = MoreExecutors
				.listeningDecorator(Executors.newFixedThreadPool(5));

		ListenableFuture<String> lf1 = les.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Integer num = 0;
				for (int i = 0; i < 20; i++) {
					System.out.println(i);
					num = +rand.nextInt(100) * i;
					Thread.sleep(200L);
//					if ( i == 5)
//						throw new RuntimeException("XXX");
				}
				return String.valueOf(num);
				// throw new RuntimeException("failed");
			}
		});
		Futures.addCallback(lf1, new FutureCallback<String>() {
			@Override
			public void onFailure(Throwable t) {
				System.out.println("lf1->failed");
				t.printStackTrace();
			}

			@Override
			public void onSuccess(String s) {
				System.out.println("num = " + s);
			}
		});

		ListenableFuture<Set<String>> lf2 = les
				.submit(new Callable<Set<String>>() {
					@Override
					public Set<String> call() throws Exception {
						Set<String> s = new HashSet<>();
						Integer num = 0;
						for (int i = 0; i < 10; i++) {
							System.out.println("s" + i);
							num = +rand.nextInt(100) * i;
							s.add(String.valueOf(num));
							Thread.sleep(100L);
						}
						return s;
						// throw new RuntimeException("failed");
					}
				});
		Futures.addCallback(lf2, new FutureCallback<Set<String>>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onSuccess(Set<String> s) {
				System.out.println("s = " + s);
			}
		});

		ListenableFuture<Integer> lf3 = Futures.transform(lf2,
				new AsyncFunction<Set<String>, Integer>() {
					@Override
					public ListenableFuture<Integer> apply(final Set<String> s)
							throws Exception {
						// since this is an AsyncFunction we are not able to use
						// 'les'... why
						return MoreExecutors.newDirectExecutorService().submit(
								new Callable<Integer>() {
									@Override
									public Integer call() throws Exception {
										return s.size();
									}
								});
					}
				});

		Futures.addCallback(lf3, new FutureCallback<Integer>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onSuccess(Integer i) {
				System.out.println("a/size = " + i);
			}
		});

		ListenableFuture<Integer> lf4 = Futures.transform(lf2,
				new Function<Set<String>, Integer>() {
					@Override
					public Integer apply(final Set<String> s) {
						return s.size();
					}
				});

		Futures.addCallback(lf4, new FutureCallback<Integer>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onSuccess(Integer i) {
				System.out.println("s/size = " + i);
			}
		});

		Collection<ListenableFuture<Integer>> c = new ArrayList<>();
		c.add(lf3);
		c.add(lf4);
		ListenableFuture<List<Integer>> lfl = Futures.allAsList(c);
		Futures.addCallback(lfl, new FutureCallback<List<Integer>>() {
			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onSuccess(List<Integer> l) {
				System.out.println("aslist = " + l);
			}
		});

		// have to shut it down, otherwise the jvm does not exit
//		les.shutdown();
		les.shutdown();
		System.out.println(les.awaitTermination(2, TimeUnit.SECONDS));
	}
}
