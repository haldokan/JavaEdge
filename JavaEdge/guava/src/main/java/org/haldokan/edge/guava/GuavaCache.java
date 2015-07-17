package org.haldokan.edge.guava;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.Maps;

public class GuavaCache {
	public static void main(String[] args) throws Exception {
		Map<String, String> storage = Maps.newHashMap();
		for (int i = 0; i < 100; i++)
			storage.put("k" + i, "v" + i);

		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
				.maximumSize(100).expireAfterWrite(10, TimeUnit.SECONDS)
				.removalListener(new RemovalListener<String, String>() {
					@Override
					public void onRemoval(RemovalNotification<String, String> n) {
						System.out.println("rmv->" + n.getKey() + ":" + n.getValue());

					}
				}).build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						System.out.println("loading " + key);
						String v = storage.get(key);
						return v == null ? key.toUpperCase() : v;
					}
				});
		for (int i = 0; i < 100; i++) {
			cache.put("kk" + i, "vv" + i);
			Thread.sleep(100L);
		}
		// expired by the time the loop for cache.put finished so it is loaded from storage (no entry so load key.toUpper)
		System.out.println(cache.get("kk0"));
		// not in cache so load
		System.out.println(cache.get("k0"));
		// now in cache -> no load
		System.out.println(cache.get("k0"));
	}
}
