package org.haldokan.edge.concurhashmap2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.Test;

public class ConcurrentHashMap2Test {
	private Random rand = new Random();

	@Test
	public void testPut() {
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>();
		String s = "foobar";
		assertThat(m.put(s, s.length()), nullValue());
		assertThat(m.put(s, s.length() * 31), is(s.length()));
	}

	@Test
	public void testGet() {
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>();
		String s = "foobar";
		m.put(s, s.length());
		assertThat(m.get(s), is(s.length()));
		m.put(s, s.length() * 31);
		assertThat(m.get(s), is(s.length() * 31));
	}

	@Test
	public void testPutAndGet() {
		// we are passing a hash function that takes the prize for dumbness
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(4, (
				String k) -> k.length(), 1);
		String s = "foobar";
		for (int i = 0; i < 10; i++) {
			m.put(s, i * s.length());
			assertThat(m.get(s), is(i * s.length()));
		}
	}

	@Test
	public void testCollisions() {
		// we are passing a hash function that takes the prize for dumbness
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(10, (
				String k) -> k.length(), 1);
		String s1 = "foobar";
		m.put(s1, s1.length());

		// same hash since it has the same lenght as s1 - collision
		String s2 = "foobaz";
		m.put(s2, 2 * s2.length());

		// same hash since it has the same lenght as s1 - collision
		System.out.println(m);
		String s3 = "hammer";
		m.put(s3, 3 * s3.length());
		System.out.println(m);

		assertThat(m.get(s1), is(s1.length()));
		assertThat(m.get(s2), is(2 * s2.length()));
		assertThat(m.get(s3), is(3 * s3.length()));
	}

	@Test
	public void testCollisionsInLoop() {
		// we are passing a hash function that takes the prize for dumbness
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(1024,
				(String k) -> k.length(), 1);
		String key = "key";
		int sampleSize = 512;
		int[] vals = new int[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			vals[i] = rand.nextInt(20);
			m.put(key + i, vals[i]);
		}
		System.out.println(m);
		for (int i = 0; i < sampleSize; i++) {
			assertThat(m.get(key + i), is(vals[i]));
		}
	}

	@Test
	public void testRemove() {
		// we are passing a hash function that takes the prize for dumbness
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(1024,
				(String k) -> k.length(), 1);
		String key = "key";
		int sampleSize = 512;
		int[] vals = new int[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			vals[i] = rand.nextInt(10);
			m.put(key + i, vals[i]);
		}
		System.out.println(m);
		for (int i = 0; i < sampleSize; i++) {
			assertThat(m.remove(key + i), is(vals[i]));
			assertThat(m.get(key + i), nullValue());
		}
		System.out.println(m);
	}

	@Test
	public void testPutRemoveGet() {
		// we are passing a hash function that takes the prize for dumbness
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(1024,
				(String k) -> k.length(), 1);
		String key = "key";
		int sampleSize = 512;
		Integer[] insertedVals = new Integer[sampleSize];

		for (int i = 0; i < sampleSize; i++) {
			insertedVals[i] = rand.nextInt(10);
			m.put(key + i, insertedVals[i]);
			if (i % 3 == 0) {
				int delIndex = i > 0 ? rand.nextInt(i) : 0;
				m.remove(key + delIndex);
				insertedVals[delIndex] = null;
			}
		}
		System.out.println(m);
		for (int i = 0; i < sampleSize; i++) {
			assertThat(m.get(key + i), is(insertedVals[i]));
		}
	}

	@Test
	public void testBitwiseHasherIntegralTypes() {
		BitwiseHasher hasher = new BitwiseHasher();
		System.out.println(hasher.apply(Long.valueOf(777)));
		System.out.println(hasher.apply(333L));
		System.out.println(hasher.apply(Integer.valueOf(777)));
	}

	@Test
	public void testBitwiseHasherDecimalTypes() {
		BitwiseHasher hasher = new BitwiseHasher();
		System.out.println(hasher.apply(Float.valueOf(777.734f)));
		System.out.println(hasher.apply(777.734f));
		System.out.println(hasher.apply(Double.valueOf(777.734d)));
		System.out.println(hasher.apply(777.734d));
	}

	@Test
	public void testBitwiseHasherStrings() {
		BitwiseHasher hasher = new BitwiseHasher();
		int hash1 = hasher
				.apply("foobar837384739842704856484359929837438473929423847328749324729874932472947923749239432942");
		// change foobar to foobaz (one letter different) - hash should not be
		// the same unless they collide
		int hash2 = hasher
				.apply("foobaz837384739842704856484359929837438473929423847328749324729874932472947923749239432942");
		// change the last digit from 2 to 1
		int hash3 = hasher
				.apply("foobaz837384739842704856484359929837438473929423847328749324729874932472947923749239432941");
		System.out.println(hash1 + "/" + hash2 + "/" + hash3);
		assertThat(hash1, not(hash2));
		assertThat(hash1, not(hash3));
		assertThat(hash2, not(hash3));
	}

	@Test
	public void testHashRangesForConcurrentAccess() throws Exception {
		int spaceSize = 1024 * 100;
		ConcurrentHashMap2<String, Integer> m = new ConcurrentHashMap2<>(
				spaceSize, k -> new BitwiseHasher().apply(k), 4);
		String key1 = "key0001";
		int sampleSize = spaceSize / 30;
		Integer[] insertedVals1 = new Integer[sampleSize];

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < sampleSize; i++) {
					insertedVals1[i] = rand.nextInt(1000);
					m.put(key1 + i, insertedVals1[i]);
					if (i % 3 == 0) {
						int delIndex = i > 0 ? rand.nextInt(i) : 0;
						m.remove(key1 + delIndex);
						insertedVals1[delIndex] = null;
					}
				}

			}
		});

		Integer[] insertedVals2 = new Integer[sampleSize];
		String key2 = "key0010";
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = sampleSize - 1; i >= 0; i--) {
					insertedVals2[i] = rand.nextInt(1000);
					m.put(key2 + i, insertedVals2[i]);
					if (i % 3 == 0) {
						int delIndex = i > 0 ? rand.nextInt(sampleSize) : 0;
						m.remove(key2 + delIndex);
						insertedVals2[delIndex] = null;
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();

		for (int i = 0; i < sampleSize; i++) {
			assertThat(m.get(key1 + i), is(insertedVals1[i]));
		}

		for (int i = sampleSize - 1; i >= 0; i--) {
			assertThat(m.get(key2 + i), is(insertedVals2[i]));
		}
	}
}
