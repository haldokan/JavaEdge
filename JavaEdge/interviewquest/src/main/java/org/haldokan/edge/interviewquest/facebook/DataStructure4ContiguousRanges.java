package org.haldokan.edge.interviewquest.facebook;

/**
 * My solution to a Facebook interview question
 * <p>
 * You are given intervals of contiguous integers, like [(1, 10), [15, 25), [40, 50), which are non-overlapping and
 * of a fixed size.
 * Design a data structure to store these intervals and have the operations of insert, delete, and find functions
 * Created by haytham.aldokanji on 5/4/16.
 */
public class DataStructure4ContiguousRanges {
    /**
     * Since range is fixed we can reduce the interval to the start value which we store. Thus (1,10)-> (1),
     * (15, 25)-> 15, etc.
     * We use a hash set for insert, delete, and find (single range) in O(1). When finding a range of intervals the hash set
     * is not appropriate (the operation is O(n)). In order to support finding range of intervals we introduce a new sorted data
     * structure, a BST for example. We use both data structures as follows:
     * find(singleRange): hashSet.find (O(1))
     * find(rangeOfIntervals): sortedDataStructure.find (O(logn))
     * insert(interval): hashset.insert (O(1)) and sortedDataStructure.insert(O(logn)) => O(logn)
     * delete(interval): hashset.delete (O(1)) and sortedDataStructure.delete(O(logn)) => O(logn)
     */
}
