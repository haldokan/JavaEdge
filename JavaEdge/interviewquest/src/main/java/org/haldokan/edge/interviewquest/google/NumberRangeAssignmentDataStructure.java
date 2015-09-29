package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * My solution to a Google interview question: Storing the 10 billion longs in memory would take 10b * 8 bytes / 1b = 80 gigabytes.
 * By using a BitSet the needed size in memory is 10b / (8 * 1b) = 1.25 gigabytes.
 * <p>
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
 * Created by haytham.aldokanji on 9/28/15.
 */
public class NumberRangeAssignmentDataStructure {
    private static final long MAX_NUM = 10_000_000_000L;
    private static final long NOT_FOUND = -1;
    private List<RangeBitVector> ranges;


    public NumberRangeAssignmentDataStructure() {
        this.ranges = splitNumberRange();
    }

    public static void main(String[] args) {
        NumberRangeAssignmentDataStructure driver = new NumberRangeAssignmentDataStructure();
//        System.out.println(driver.ranges);
        System.out.println(driver.getNumber());
        long num = Integer.MAX_VALUE - 5L;
        System.out.println(driver.requestNumber(num));
        num = Integer.MAX_VALUE + 5L;
        System.out.println(driver.requestNumber(num));
        System.out.println(driver.requestNumber(num));
        System.out.println(driver.getNumber());

        // It takes a bout 5-10 seconds to iterate thru the loop
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            driver.requestNumber(i);
        }
        // should be printing numbers >= Integer.MAX_VALUE
        System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
        for (int i = 0; i < 10; i++) {
            System.out.println(driver.getNumber());
        }
    }

    /**
     * Loop thru the ranges until a number can be optained.
     *
     * @return
     */
    public long getNumber() {
        for (RangeBitVector range : ranges) {
            long num = range.getNumber();
            if (num != NOT_FOUND)
                return num;
        }
        throw new RuntimeException("Could not assign a number: All numbers are assigned");
    }

    /**
     * Find the range that contains the number and and assign the number
     *
     * @param number
     * @return
     */
    public boolean requestNumber(long number) {
        for (RangeBitVector range : ranges) {
            if (range.inRange(number)) {
                return range.requestNumber(number);
            }
        }
        return false;
    }

    /**
     * We cannot create a BitSet (or an array for that matter) larger than Integer.MAX_VALUE. We solve that by segmenting
     * the MAX_NUM range to multiple ranges of length Integer.MAX_VALUE
     *
     * @return
     */
    private List<RangeBitVector> splitNumberRange() {
        int numOfRanges = (int) (MAX_NUM / Integer.MAX_VALUE);
        int partialRangeSize = (int) (MAX_NUM % Integer.MAX_VALUE);

        List<RangeBitVector> ranges = new ArrayList<>();
        // for loop index type is long so the int args passed to RangeBitVector do not overflow
        for (long i = 0; i < numOfRanges; i++) {
            ranges.add(new RangeBitVector(i * Integer.MAX_VALUE, (i + 1) * Integer.MAX_VALUE));
        }
        if (partialRangeSize > 0) {
            long previousRangeHigh = ranges.get(ranges.size() - 1).high;
            ranges.add(new RangeBitVector(previousRangeHigh, previousRangeHigh + partialRangeSize));
        }
        return ranges;
    }

    private static class RangeBitVector {
        private long low, high;
        private int size;
        private int current;
        private BitSet bitVector;

        public RangeBitVector(long low, long high) {
            this.low = low;
            this.high = high;
            this.size = (int) (high - low);
            this.bitVector = new BitSet();
        }

        public boolean inRange(long num) {
            return low <= num && num < high;
        }

        public long getNumber() {
            while (bitVector.get(current) && current < size) {
                current++;
            }
            if (current < size) {
                bitVector.set(current);
                return low + current;
            }
            return NOT_FOUND;
        }

        public boolean requestNumber(long num) {
            if (!inRange(num))
                throw new IllegalArgumentException("Requested number is out of range " + num);
            int index = (int) (num - low);
            if (bitVector.get(index)) {
                return false;
            } else {
                bitVector.set(index);
                return true;
            }
        }

        @Override
        public String toString() {
            return "RangeBitVector{" +
                    "low=" + low +
                    ", high=" + high +
                    ", size=" + size +
                    ", current=" + current +
                    '}';
        }
    }
}
