package org.haldokan.edge.concurhashmap2;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

// TODO defect: if an insert run reaches the end of a range and finds it taken hashing fails. 
// It may be better to have separate hash arrays for each range and support hash space extension.
public class ConcurrentHashMap2<K, V> {
	private static final int SLOT_NOT_FOUND = -1;
	private HashEntry<K, V>[] hashSpace;
	private final Function<K, Integer> hashFunc;
	private final HashRange[] hashRanges;

	public ConcurrentHashMap2() {
		// supply our home-grown bitwise hasher
		this(1024 * 1024, k -> new BitwiseHasher().apply(k), 4);
	}

	public ConcurrentHashMap2(int hashSpaceSize, Function<K, Integer> hashFunc, int concurrencyLevel) {
		// no way around this cast warning
		this.hashSpace = new HashEntry[pow2Len(hashSpaceSize)];
		this.hashFunc = hashFunc;
		this.hashRanges = createHashRanges(concurrencyLevel);
	}

	// Handle collisions using open addressing (good for insert but bad for
	// delete if the hash space is saturated)
	public V put(K k, V v) {
		int hashIndex  = hashIndex(k);
		HashRange range = getHashRange(hashIndex);
		range.rwLock.writeLock().lock();
		try {
			int slot = findPutSlot(k, v, hashIndex, range);
			if (slot == SLOT_NOT_FOUND)
				throw new RuntimeException(
						"Hash space is saturated - cannot hash value with open addressing: "
								+ k + "/" + v);

			HashEntry<K, V> oldVal = hashSpace[slot];
			hashSpace[slot] = new HashEntry<K, V>(k, v);
			return oldVal == null ? null : oldVal.v;
		} finally {
			range.rwLock.writeLock().unlock();
		}
	}

	public V get(K k) {
		int hashIndex  = hashIndex(k);
		HashRange range = getHashRange(hashIndex);
		range.rwLock.readLock().lock();
		try {
			int slot = findGetSlot(k, hashIndex, range);
			if (slot == SLOT_NOT_FOUND)
				return null;
			return hashSpace[slot].v;
		} finally {
			range.rwLock.readLock().unlock();
		}
	}

	public V remove(K k) {
		int hashIndex  = hashIndex(k);
		HashRange range = getHashRange(hashIndex);
		range.rwLock.writeLock().lock();
		try {
			int slot = findGetSlot(k, hashIndex, range);
			if (slot == SLOT_NOT_FOUND)
				return null;
			V rmvVal = hashSpace[slot].v;
			hashSpace[slot] = null;
			reinsertRunAfterRemovedSlot(k, slot, range);
			return rmvVal;
		} finally {
			range.rwLock.writeLock().unlock();
		}
	}
	
	// removal from open-addressing hashspace can create gaps making entries
	// unaccessible
	// we want to re-insert any entry runs that happen after gap
	private void reinsertRunAfterRemovedSlot(K k, int slot, HashRange range) {
		if (slot == 0 || slot == (range.high - 1)
				|| hashSpace[slot + 1] == null)
			return;
		for (int i = slot + 1; i < range.high && hashSpace[i] != null; i++) {
			HashEntry<K, V> entry = hashSpace[i];
			if (Math.abs(hashFunc.apply(entry.k)) != i) {
				hashSpace[i] = null;
				put(entry.k, entry.v);
			}
		}
	}

	private int findGetSlot(K k, int hashIndex, HashRange range) {
		int slot = SLOT_NOT_FOUND;
		for (int i = hashIndex; i < range.high; i++) {
			if (hashSpace[i] == null)
				break;
			if (hashSpace[i].k.equals(k)) {
				slot = i;
				break;
			}
		}
		return slot;
	}

	private int findPutSlot(K k, V v, int hashIndex, HashRange range) {
		int slot = SLOT_NOT_FOUND;
		for (int i = hashIndex; i < range.high; i++) {
			if (hashSpace[i] == null || hashSpace[i].k.equals(k)) {
				slot = i;
				break;
			}
		}
		return slot;
	}
	
	private HashRange[] createHashRanges(int concurrencyLevel) {
		HashRange[] hashRanges = new HashRange[concurrencyLevel];
		
		int rangeSize = hashSpace.length / hashRanges.length;
		int rangeSizeRemainder = hashSpace.length % hashRanges.length;
		
		int low = 0;
		for (int i = 0; i < hashRanges.length; i++) {
			int high = low + rangeSize;
			
			// in case the the hash space was not multiple of the concurrency level add the remainder
			if (i + 1 == hashRanges.length)
				high += rangeSizeRemainder;
			hashRanges[i] = new HashRange(low, high);
			low = high;
		}
		return hashRanges;
	}
	
	private HashRange getHashRange(int hash) {
		for (HashRange range : hashRanges) {
			if (range.isInRange(hash))
				return range;
		}
		throw new RuntimeException("Inconsistent state. Cannot find hash range for hash value " + hash);
	}
	
	private int hashIndex(K k) {
		return Math.abs(hashFunc.apply(k) % hashSpace.length);
	}
	
	private int pow2Len(int len) {
		double log2Len = Math.ceil(32 - Integer.numberOfLeadingZeros(len - 1));
		return (int) Math.pow(2, log2Len);
	}

	@Override
	public String toString() {
		return "ConcurrentHashMap2 [hashSpace=" + Arrays.toString(hashSpace)
				+ ", hashRanges=" + Arrays.toString(hashRanges) + "]";
	}


	private static class HashEntry<K, V> {
		private final K k;
		private final V v;

		public HashEntry(K k, V v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public String toString() {
			return "[" + k + "," + v + "]";
		}
	}
	
	private static class HashRange {
		private final int low, high;
		private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		
		public HashRange(int low, int high) {
			this.low = low;
			this.high = high;
		}
		
		public boolean isInRange(int hash) {
			return hash >= low && hash < high;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "HashRange [low=" + low + ", high=" + high + "]";
		}
	}
}
