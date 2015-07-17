package org.haldokan.edge.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public class GuavaMultiCollections {

	public static void main(String[] args) {
		Multiset<GadgetMetric> gmset = HashMultiset.create();
		// the 2 'phone' gadgets are considered the same and only one is
		// retained: the one with metric 20 is gone.
		gmset.add(new GadgetMetric("phone", 1L, 10));
		gmset.add(new GadgetMetric("phone", 2L, 20));
		gmset.add(new GadgetMetric("server", 1L, 1000_000));

		System.out.println(gmset.elementSet());
		System.out.println(gmset.entrySet());

		for (Entry<GadgetMetric> gme : gmset.entrySet())
			System.out.println(gme.getElement() + " - " + gme.getCount());
		
		//multi-map
		System.out.println("list mmap");
		Multimap<String, String> lm = ArrayListMultimap.create();
		lm.put("haldokan", "engineer");
		lm.put("haldokan", "engineer");
		lm.put("haldokan", "athlete");
		lm.put("foobar", "baz");
		
		System.out.println(lm.get("haldokan"));
		System.out.println(lm.get("absent"));
		
		System.out.println("set mmap");
		Multimap<String, String> sm = HashMultimap.create();
		sm.put("haldokan", "engineer");
		sm.put("haldokan", "engineer");
		sm.put("haldokan", "athlete");
		sm.put("foobar", "baz");
		
		System.out.println(sm.get("haldokan"));
		System.out.println(sm.get("absent"));
		System.out.println(sm.keys());
		System.out.println(sm.keySet());
	}

	private static class GadgetMetric {
		private String gid;
		private Long ticker;
		private Integer metric;

		public GadgetMetric(String gid, Long ticker, Integer metric) {
			this.gid = gid;
			this.ticker = ticker;
			this.metric = metric;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((gid == null) ? 0 : gid.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GadgetMetric other = (GadgetMetric) obj;
			if (gid == null) {
				if (other.gid != null)
					return false;
			} else if (!gid.equals(other.gid))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "GadgetMetric [gid=" + gid + ", ticker=" + ticker
					+ ", metric=" + metric + "]";
		}

	}
}
