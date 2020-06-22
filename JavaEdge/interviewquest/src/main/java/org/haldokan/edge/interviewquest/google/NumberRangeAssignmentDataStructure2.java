package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * My solution to a Google interview question: Me be the concern is not memory (which drove my solution NumberRangeAssignmentDataStructure)
 * In this case performance is the overriding concern. We achieve requestNumber in O(1) and getNumber in O(n) where n is
 * the size of the currently assigned numbers (asymptotically it is the max value given in the question)
 *
 * The Question: 4_STAR
 *
 * We have numbers in between 0-9_999_999_999 (10-digits) which are assigned someone (does not matter which number assigned who)
 * <p>
 * Write two methods called "getNumber" and "requestNumber" as follows
 * Number getNumber();
 * boolean requestNumber(Number number);
 * <p>
 * getNumber method should find out a number that is not assigned then mark it as assigned and return that number.
 * <p>
 * requestNumber method checks the number is assigned or not. If it is assigned returns false, else marks it as assigned and return true.
 * <p>
 * design a data structure to keep those numbers and implement those methods
 * <p>
 * Created by haytham.aldokanji on 6/19/2020.
 */
public class NumberRangeAssignmentDataStructure2 {
    SortedSet<Long> assignedNums = new TreeSet<>();
    long min;
    long max;

    public static void main(String[] args) {
        NumberRangeAssignmentDataStructure2 driver = new NumberRangeAssignmentDataStructure2(0, 10);

        System.out.println(driver.reqNum(driver.min));
        System.out.println(driver.reqNum(driver.max));
        System.out.println(driver.assignedNums);

        System.out.println(driver.getNum());
        System.out.println(driver.getNum());
        System.out.println(driver.getNum());
        System.out.println(driver.assignedNums);
        System.out.println(driver.reqNum(2));
    }

    public NumberRangeAssignmentDataStructure2(long min, long max) {
        this.min = min;
        this.max = max;
    }

    // O(n)
    long getNum() {
        if (assignedNums.isEmpty() || assignedNums.first() != min) {
            assignedNums.add(min);
            return min;
        } else if (assignedNums.last() != max) {
            assignedNums.add(max);
            return max;
        }
        Iterator<Long> it = assignedNums.iterator();
        long first = it.next();
        long num = -1;
        for (it = assignedNums.iterator(); it.hasNext();) {
            long next = it.next();
            if (next - first > 1) {
                num = first + 1;
                assignedNums.add(num);
                break;
            } else {
                first = next;
            }
        }
        // -1 indicate that all numbers are assigned
        return num;
    }

    // O(1)
    boolean reqNum(long num) {
        if (assignedNums.contains(num)) {
            return false;
        }
        assignedNums.add(num);
        return true;
    }
}
