package org.haldokan.edge.guava;

import com.google.common.hash.BloomFilter;

public class GuavaBloomFilter {
	public static void main(String[] args) {
		BloomFilter<Account> bf = BloomFilter.create(GuavaFunnel.INSTANCE, 10000, 0.03);
		Account a1 = new Account("Haytham", "a1", 1000);
		Account a2 = new Account("Benita", "a1", 2000);
		Account a3 = new Account("Hamza", "a3", 3000);
		
		bf.put(a1);
		bf.put(a2);
		
		System.out.println(bf.mightContain(a1));
		System.out.println(bf.mightContain(a2));
		System.out.println(bf.mightContain(a3));
		System.out.println(bf.expectedFpp());
		
		for (int i = 0; i < 8000; i++)
			bf.put(new Account("owner" + i, "id" + i, i * 100));
		
		System.out.println(bf.expectedFpp());
	}
}
