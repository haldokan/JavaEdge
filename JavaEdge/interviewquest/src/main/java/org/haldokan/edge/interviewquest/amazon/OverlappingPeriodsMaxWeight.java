package org.haldokan.edge.interviewquest.amazon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * My solution to an Amazon interview questoin
 * <p>
 * There are discounts on particular time period
 * suppost
 * Day1 - Day5 => 10%
 * Day2 - Day8 => 5%
 * Day4 - Day6 => 20 %
 * <p>
 * find the period where maximum discounts is available.
 * <p>
 * For above example the period is Day4 - Day5 => 10+5+20
 * <p>
 * that means 35%
 * <p>
 * Provide the generalize solution. Period can be time also.
 */

public class OverlappingPeriodsMaxWeight {
    private List<Period> periods = new ArrayList<>();

    public static void main(String[] args) {
        // of course tests should have asserts in a real app (rather than eye-balling the results)
        OverlappingPeriodsMaxWeight case1 = new OverlappingPeriodsMaxWeight();
        case1.test1();

        OverlappingPeriodsMaxWeight case2 = new OverlappingPeriodsMaxWeight();
        case2.test2();

        OverlappingPeriodsMaxWeight case3 = new OverlappingPeriodsMaxWeight();
        case3.test3();

        OverlappingPeriodsMaxWeight case4 = new OverlappingPeriodsMaxWeight();
        case4.test4();

    }

    private void add(Period newPeriod) {
        if (periods.isEmpty()) {
            periods.add(newPeriod);
        } else {
            List<Period> newPeriods = new ArrayList<>();

            for (Iterator<Period> it = periods.iterator(); it.hasNext(); ) {
                Period period = it.next();
                List<Period> combined = combine(period, newPeriod);

                if (combined.size() > 1) {
                    it.remove();
                }
                newPeriods.addAll(combined);
            }
            periods.addAll(newPeriods);
        }
    }

    public List<Period> combine(Period p1, Period p2) {
        // if empty then same period
        // if size = 1 then disjoint
        // if size > 1 then intersect which means the original period should be removed
        List<Period> combined = new ArrayList<>();
        if (p1.same(p2)) {
            p1.increaseDiscount(p2.discount);
        } else if (p1.disjoint(p2)) {
            combined.add(p2);
        } else if (p1.contains(p2)) {
            combined.addAll(containedParts(p1, p2));
        } else if (p2.contains(p1)) {
            combined.addAll(containedParts(p2, p1));
        } else if (p1.dovetails(p2)) {
            combined.addAll(dovtailedParts(p1, p2));
        } else if (p2.dovetails(p1)) {
            combined.addAll(dovtailedParts(p2, p1));
        } else {
            throw new RuntimeException("Unaccounted for period arrangement: " + p1 + "\n" + p2);
        }

        return combined;
    }

    private List<Period> containedParts(Period p1, Period p2) {
        List<Period> rslt = new ArrayList<>();

        Period part1 = new Period(p1.discount, p1.start, p2.start);
        if (part1.valid) {
            rslt.add(part1);
        }

        Period part2 = new Period(p1.discount + p2.discount, p2.start, p2.end);
        rslt.add(part2);

        Period part3 = new Period(p1.discount, p2.end, p1.end);
        if (part3.valid) {
            rslt.add(part3);
        }
        return rslt;
    }

    private List<Period> dovtailedParts(Period p1, Period p2) {
        List<Period> rslt = new ArrayList<>();

        Period part1 = new Period(p1.discount, p1.start, p2.start);
        rslt.add(part1);

        Period part2 = new Period(p1.discount + p2.discount, p2.start, p1.end);
        rslt.add(part2);

        Period part3 = new Period(p2.discount, p1.end, p2.end);
        rslt.add(part3);

        return rslt;
    }

    private void test1() {

        Period p1 = new Period(10, LocalDate.now(), LocalDate.now().plusDays(5));
        add(p1);

        Period p2 = new Period(5, LocalDate.now().plusDays(1), LocalDate.now().plusDays(8));
        add(p2);

        Period p3 = new Period(20, LocalDate.now().plusDays(3), LocalDate.now().plusDays(6));
        add(p3);

        periods.sort((v1, v2) -> v2.discount - v1.discount);
//        System.out.println(driver.periods);
        System.out.println(periods.get(0));

    }

    private void test2() {

        Period p1 = new Period(10, LocalDate.now(), LocalDate.now().plusDays(5));
        add(p1);

        Period p2 = new Period(5, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        add(p2);

        periods.sort((v1, v2) -> v2.discount - v1.discount);
//        System.out.println(driver.periods);
        System.out.println(periods.get(0));

    }

    private void test3() {

        Period p1 = new Period(10, LocalDate.now(), LocalDate.now().plusDays(5));
        add(p1);

        Period p2 = new Period(5, LocalDate.now(), LocalDate.now().plusDays(2));
        add(p2);

        periods.sort((v1, v2) -> v2.discount - v1.discount);
//        System.out.println(driver.periods);
        System.out.println(periods.get(0));

    }

    private void test4() {

        Period p1 = new Period(10, LocalDate.now(), LocalDate.now().plusDays(5));
        add(p1);

        Period p2 = new Period(5, LocalDate.now().plusDays(2), LocalDate.now().plusDays(3));
        add(p2);

        periods.sort((v1, v2) -> v2.discount - v1.discount);
//        System.out.println(driver.periods);
        System.out.println(periods.get(0));

    }

    private static class Period {
        private final LocalDate start, end;
        private Integer discount;
        private boolean valid;

        public Period(Integer discount, LocalDate start, LocalDate end) {
            this.discount = discount;
            this.start = start;
            this.end = end;
            this.valid = end.isAfter(start);
        }

        public Period(Period p) {
            this.discount = p.discount;
            this.start = p.start;
            this.end = p.end;
        }

        public void increaseDiscount(Integer amount) {
            this.discount += amount;
        }

        public boolean disjoint(Period p) {
            return p.start.isAfter(end) || start.isAfter(p.end);
        }

        public boolean dovetails(Period p) {
            return start.isBefore(p.start) && end.isBefore(p.end) && end.isAfter(p.start);
        }

        public boolean same(Period p) {
            return start.isEqual(p.start) && end.isEqual(p.end);
        }

        public boolean contains(Period p) {
            return (start.isBefore(p.start) || start.isEqual(p.start)) &&
                    (end.isEqual(p.end) || end.isAfter(p.end));
        }

        @Override
        public String toString() {
            return "\n" + discount + "@" + start + "/" + end;
        }
    }
}
