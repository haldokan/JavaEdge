package org.haldokan.edge.guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class GuavaBiMap {
    public static void main(String[] args) {
	BiMap<Integer, String> bm = HashBiMap.create();
	bm.put(1, "s1");
	bm.put(2, "s2");

	System.out.println(bm.get(1));
	System.out.println(bm.inverse().get("s1"));
	System.out.println(bm.getOrDefault(77, "s0"));

	// this throws and exception
	// bm.put(3, "s2");
	// this forces the new mapping
	System.out.println(bm.get(2));
	bm.forcePut(3, "s2");
	System.out.println(bm.get(2));
	System.out.println(bm.get(3));
	System.out.println(bm.inverse().get("s2"));
	// after this key 2 is gone
	bm.forcePut(1, "s2");
	System.out.println(bm);
    }
}
