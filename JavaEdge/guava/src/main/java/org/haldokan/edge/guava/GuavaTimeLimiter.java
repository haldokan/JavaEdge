package org.haldokan.edge.guava;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

public class GuavaTimeLimiter implements IGuavaTimeLimiter {
	public static void main(String[] args) throws Exception {
		TimeLimiter tl = new SimpleTimeLimiter();
		IGuavaTimeLimiter proxy = tl.newProxy(new GuavaTimeLimiter(), IGuavaTimeLimiter.class, 100, TimeUnit.MILLISECONDS);
		try {
			//not that if we switch the order of these 2 lines only the 200L will be printed
			System.out.println(proxy.m("foobar", 50L));
			System.out.println(proxy.m("foobar", 200L));
		} catch (UncheckedTimeoutException e) {
			System.out.println("defalut");
		}
	}
	
	@Override
	public String m(String s, long stime) throws InterruptedException {
		Thread.sleep(stime);
		return s.toUpperCase();
	}
}


