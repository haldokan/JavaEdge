package org.haldokan.edge.interviewquest.linkedin;

import java.util.List;

/**
 * My solution to a Linkedin interview question. It is hard to test it since they provide on an interface w/o
 * implementation. But I think I present here a valid approach that relies on recursion b/w 2 methods
 *
 * @author haldokan
 */
public class SumOfNestedListsWeightedByDepth {

    /**
     * Given a nested list of integers, returns the sum of all integers in the list weighted by their depth For example,
     * given the list {{1,1},2,{1,1}} the function should return 10 (four 1's at depth 2, one 2 at depth 1) Given the
     * list {1,{4,{6}}} the function should return 27 (one 1 at depth 1, one 4 at depth 2, and one 6 at depth 3)
     */
    public int depthSum(List<NestedInteger> input) { // ur implementation here
        int sum = 0;
        for (NestedInteger ne : input)
            sum += nestedIntListSum(ne, 1);
        return sum;
    }

    private int nestedIntListSum(NestedInteger ne, int nlevel) {
        if (ne.isInteger())
            return nlevel * ne.getInteger();
        else
            return doDepthSum(ne.getList(), ++nlevel);

    }

    private int doDepthSum(List<NestedInteger> list, int i) {
        if (list.size() == 1)
            return nestedIntListSum(list.get(0), i);

        return nestedIntListSum(list.remove(0), i) + doDepthSum(list, i);
    }

    /**
     * This is the interface that represents nested lists. You should not implement it, or speculate about its
     * implementation.
     */
    private interface NestedInteger {
        /**
         * @return true if this NestedInteger holds a single integer, rather than a nested list
         */
        boolean isInteger();

        /**
         * @return the single integer that this NestedInteger holds, if it holds a single integer. Return null if this
         * NestedInteger holds a nested list
         */
        Integer getInteger();

        /**
         * @return the nested list that this NestedInteger holds, if it holds a nested list. Return null if this
         * NestedInteger holds a single integer
         */
        List<NestedInteger> getList();
    }
}