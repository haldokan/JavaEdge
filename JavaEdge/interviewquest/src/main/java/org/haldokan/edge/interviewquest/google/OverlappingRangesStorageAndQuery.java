package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - add the ranges to a sorted set by the start index. At each addition of
 * a range intersect it with all the already existing ranges and create new ranges when there is overlapping. New ranges
 * replace the old overlapping ranges. The final set of ranges will not have any overlapped ranges. Each range has the set
 * of tags associated with it. Finding the ranges associated with a set of tags requires scanning the set of ranges in
 * O(n) and returning the ranges with tag sets containing the search tag set.
 *
 * The Question: 4_STAR
 *
 * We have a long string. We label some substrings with tags.
 * <p>
 * - A tag entry is [startIndex, endIndex, tag].
 * - Query: 1 or more tags
 * - Output: all blocks/ranges with all queried tags.
 * <p>
 * Example tag entries:
 * <p>
 * [23, 72, 0]	// label [23, 72) with tag 0
 * [34, 53, 1]	// label [34, 53) with tag 1
 * [100, 128, 0]
 * <p>
 * Query and Output:
 * <p>
 * 0 => [23, 72], [100, 128]
 * 0,1 => [34,53]	// [34, 53) matches both tag 0 and 1
 * <p>
 * Give an efficient algorithm. Please describe your algorithm before posting code.
 * <p>
 * *Edit**: To add some difficulties, partial overlap is treated the same as full overlap, ONLY the overlapped part
 * matches both tags. E.g. if we have entries:
 * <p>
 * [23, 72, 0]	// label [23, 72) with tag 0
 * [10, 53, 1]	// label [34, 53) with tag 1
 * <p>
 * Query and Output:
 * 0,1 => [23,53]	// [23, 53) matches both tag 0 and 1
 * <p>
 * Minor detials: Note in the comments we used open range on the right, i.e. if the string named "str", [23, 72, 0]
 * includes str[23] but NOT str[72]; and there's no overlap between the following entries:
 * [23, 72, 0]
 * [10, 23, 0]
 * Created by haytham.aldokanji on 5/15/16.
 */
public class OverlappingRangesStorageAndQuery {
    private Set<Range> ranges = new TreeSet<>((v1, v2) -> v1.start - v2.start);

    public static void main(String[] args) {
        OverlappingRangesStorageAndQuery driver = new OverlappingRangesStorageAndQuery();

        driver.testRangeOverlapping();
        driver.testRangeInnerContainment();
        driver.testRangeLeftContainment();
        driver.testRangeRightContainment();
        driver.testNewRangeOverlappingWithExistingRange();
        driver.testRangeOverlappingAndContainment();
        driver.testFindTagsRanges();
    }

    public Optional<List<Range>> find(Integer... tags) {
        Set<Integer> tagSet = Arrays.stream(tags).collect(Collectors.toSet());
        List<Range> matching = ranges.stream()
                .filter(range -> Sets.difference(tagSet, range.tags).isEmpty())
                .map(Range::copy)
                .collect(Collectors.toList());

        return matching.isEmpty() ? Optional.empty() : Optional.of(matching);
    }

    public void add(Range newRange) {
        if (ranges.isEmpty()) {
            ranges.add(newRange);
        } else {
            List<Range> newRanges = new ArrayList<>();
            Range newRangeCopy = newRange;
            for (Iterator<Range> it = ranges.iterator(); it.hasNext(); ) {
                Range range = it.next();
                CombinedResult combined = combine(range, newRangeCopy);

                if (combined.hasResults()) {
                    it.remove();
                    newRanges.addAll(combined.ranges);
                }
                if (combined.rangeProcessed()) {
                    break;
                }
                newRangeCopy = combined.carryRange.get();
            }
            if (newRangeCopy != null) {
                newRanges.add(newRangeCopy);
            }
            ranges.addAll(newRanges);
        }
    }

    public CombinedResult combine(Range existingRange, Range newRange) {
        CombinedResult combinedResult = CombinedResult.create();
        if (existingRange.same(newRange)) {
            existingRange.addTags(newRange.tags);
        } else if (existingRange.disjoint(newRange)) {
            combinedResult.updateRange(newRange);
        } else if (existingRange.contains(newRange)) {
            combinedResult.addResults(containedParts(existingRange, newRange));
        } else if (newRange.contains(existingRange)) {
            List<Range> combinedResults = containedParts(newRange, existingRange);
            Range updatedRange = combinedResults.remove(combinedResults.size() - 1);
            combinedResult.addResults(combinedResults).updateRange(updatedRange);
        } else if (existingRange.dovetails(newRange)) {
            List<Range> combinedResults = dovtailedParts(existingRange, newRange);
            Range updatedRange = combinedResults.remove(combinedResults.size() - 1);
            combinedResult.addResults(combinedResults).updateRange(updatedRange);
        } else if (newRange.dovetails(existingRange)) {
            combinedResult.addResults(dovtailedParts(newRange, existingRange));
        } else {
            throw new RuntimeException("Unaccounted for period arrangement: " + existingRange + "\n" + newRange);
        }
        return combinedResult;
    }

    private List<Range> containedParts(Range r1, Range r2) {
        List<Range> rslt = new ArrayList<>();

        Range part1 = new Range(r1.tags, r1.start, r2.start);
        if (part1.valid) {
            rslt.add(part1);
        }

        Range part2 = new Range(r1.tags, r2.start, r2.end).addTags(r2.tags);
        rslt.add(part2);

        Range part3 = new Range(r1.tags, r2.end, r1.end);
        if (part3.valid) {
            rslt.add(part3);
        }
        return rslt;
    }

    private List<Range> dovtailedParts(Range r1, Range r2) {
        List<Range> rslt = new ArrayList<>();

        Range part1 = new Range(r1.tags, r1.start, r2.start);
        rslt.add(part1);

        Range part2 = new Range(r1.tags, r2.start, r1.end).addTags(r2.tags);
        rslt.add(part2);

        Range part3 = new Range(r2.tags, r1.end, r2.end);
        rslt.add(part3);

        return rslt;
    }

    private void testRangeOverlapping() {
        ranges.clear();
        Range r1 = new Range(1, 5).addTag(0);
        add(r1);

        Range r2 = new Range(2, 9).addTag(1);
        add(r2);

        Range r3 = new Range(4, 7).addTag(2);
        add(r3);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(1, 2)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(0)));

        assertThat(Iterables.get(ranges, 1), is(new Range(2, 4)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0, 1)));

        assertThat(Iterables.get(ranges, 2), is(new Range(4, 5)));
        assertThat(Iterables.get(ranges, 2).tags, is(ImmutableSet.of(0, 1, 2)));

        assertThat(Iterables.get(ranges, 3), is(new Range(5, 7)));
        assertThat(Iterables.get(ranges, 3).tags, is(ImmutableSet.of(1, 2)));

        assertThat(Iterables.get(ranges, 4), is(new Range(7, 9)));
        assertThat(Iterables.get(ranges, 4).tags, is(ImmutableSet.of(1)));
    }

    private void testNewRangeOverlappingWithExistingRange() {
        ranges.clear();
        Range r1 = new Range(3, 6).addTag(0);
        add(r1);

        Range r2 = new Range(1, 4).addTag(1);
        add(r2);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(1, 3)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(1)));

        assertThat(Iterables.get(ranges, 1), is(new Range(3, 4)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0, 1)));

        assertThat(Iterables.get(ranges, 2), is(new Range(4, 6)));
        assertThat(Iterables.get(ranges, 2).tags, is(ImmutableSet.of(0)));
    }

    private void testRangeInnerContainment() {
        ranges.clear();

        Range r1 = new Range(1, 6).addTag(0);
        add(r1);

        Range r2 = new Range(2, 3).addTag(1);
        add(r2);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(1, 2)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(0)));

        assertThat(Iterables.get(ranges, 1), is(new Range(2, 3)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0, 1)));

        assertThat(Iterables.get(ranges, 2), is(new Range(3, 6)));
        assertThat(Iterables.get(ranges, 2).tags, is(ImmutableSet.of(0)));
    }

    private void testRangeLeftContainment() {
        ranges.clear();

        Range r1 = new Range(1, 5).addTag(0);
        add(r1);

        Range r2 = new Range(1, 3).addTag(1);
        add(r2);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(1, 3)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(0, 1)));

        assertThat(Iterables.get(ranges, 1), is(new Range(3, 5)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0)));
    }

    private void testRangeRightContainment() {
        ranges.clear();

        Range r1 = new Range(1, 6).addTag(0);
        add(r1);

        Range r2 = new Range(3, 6).addTag(1);
        add(r2);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(1, 3)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(0)));

        assertThat(Iterables.get(ranges, 1), is(new Range(3, 6)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0, 1)));
    }

    private void testRangeOverlappingAndContainment() {
        ranges.clear();
        Range r1 = new Range(1, 5).addTag(0);
        add(r1);

        Range r2 = new Range(2, 9).addTag(1);
        add(r2);

        Range r3 = new Range(4, 7).addTag(2);
        add(r3);

        //contains the previous 3 ranges
        Range r4 = new Range(0, 10).addTag(3);
        add(r4);

        System.out.println(ranges);
        assertThat(Iterables.get(ranges, 0), is(new Range(0, 1)));
        assertThat(Iterables.get(ranges, 0).tags, is(ImmutableSet.of(3)));

        assertThat(Iterables.get(ranges, 1), is(new Range(1, 2)));
        assertThat(Iterables.get(ranges, 1).tags, is(ImmutableSet.of(0, 3)));

        assertThat(Iterables.get(ranges, 2), is(new Range(2, 4)));
        assertThat(Iterables.get(ranges, 2).tags, is(ImmutableSet.of(0, 1, 3)));

        assertThat(Iterables.get(ranges, 3), is(new Range(4, 5)));
        assertThat(Iterables.get(ranges, 3).tags, is(ImmutableSet.of(0, 1, 2, 3)));

        assertThat(Iterables.get(ranges, 4), is(new Range(5, 7)));
        assertThat(Iterables.get(ranges, 4).tags, is(ImmutableSet.of(1, 2, 3)));

        assertThat(Iterables.get(ranges, 5), is(new Range(7, 9)));
        assertThat(Iterables.get(ranges, 5).tags, is(ImmutableSet.of(1, 3)));

        assertThat(Iterables.get(ranges, 6), is(new Range(9, 10)));
        assertThat(Iterables.get(ranges, 6).tags, is(ImmutableSet.of(3)));
    }

    private void testFindTagsRanges() {
        ranges.clear();
        Range r1 = new Range(1, 5).addTag(0);
        add(r1);

        Range r2 = new Range(2, 9).addTag(1);
        add(r2);

        Range r3 = new Range(4, 7).addTag(2);
        add(r3);

        List<Range> matching = find(0).get();
        System.out.println(matching);
        assertThat(matching.size(), is(3));
        assertThat(Iterables.get(matching, 0), is(new Range(1, 2)));
        assertThat(Iterables.get(matching, 0).tags, is(ImmutableSet.of(0)));
        assertThat(Iterables.get(matching, 1), is(new Range(2, 4)));
        assertThat(Iterables.get(matching, 1).tags, is(ImmutableSet.of(0, 1)));
        assertThat(Iterables.get(matching, 2), is(new Range(4, 5)));
        assertThat(Iterables.get(matching, 2).tags, is(ImmutableSet.of(0, 1, 2)));

        matching = find(0, 1).get();
        System.out.println(matching);
        assertThat(matching.size(), is(2));
        assertThat(Iterables.get(matching, 0), is(new Range(2, 4)));
        assertThat(Iterables.get(matching, 0).tags, is(ImmutableSet.of(0, 1)));
        assertThat(Iterables.get(matching, 1), is(new Range(4, 5)));
        assertThat(Iterables.get(matching, 1).tags, is(ImmutableSet.of(0, 1, 2)));

        matching = find(0, 1, 2).get();
        System.out.println(matching);
        assertThat(matching.size(), is(1));
        assertThat(Iterables.get(matching, 0), is(new Range(4, 5)));
        assertThat(Iterables.get(matching, 0).tags, is(ImmutableSet.of(0, 1, 2)));

        matching = find(1).get();
        System.out.println(matching);
        assertThat(matching.size(), is(4));
        assertThat(Iterables.get(matching, 0), is(new Range(2, 4)));
        assertThat(Iterables.get(matching, 0).tags, is(ImmutableSet.of(0, 1)));
        assertThat(Iterables.get(matching, 1), is(new Range(4, 5)));
        assertThat(Iterables.get(matching, 1).tags, is(ImmutableSet.of(0, 1, 2)));
        assertThat(Iterables.get(matching, 2), is(new Range(5, 7)));
        assertThat(Iterables.get(matching, 2).tags, is(ImmutableSet.of(1, 2)));
        assertThat(Iterables.get(matching, 3), is(new Range(7, 9)));
        assertThat(Iterables.get(matching, 3).tags, is(ImmutableSet.of(1)));

        matching = find(1, 2).get();
        System.out.println(matching);
        assertThat(matching.size(), is(2));
        assertThat(Iterables.get(matching, 0), is(new Range(4, 5)));
        assertThat(Iterables.get(matching, 0).tags, is(ImmutableSet.of(0, 1, 2)));
        assertThat(Iterables.get(matching, 1), is(new Range(5, 7)));
        assertThat(Iterables.get(matching, 1).tags, is(ImmutableSet.of(1, 2)));
    }


    private static class CombinedResult {
        private List<Range> ranges = new ArrayList<>();
        private Optional<Range> carryRange = Optional.empty();

        private CombinedResult() {
        }

        public static CombinedResult create() {
            return new CombinedResult();
        }

        public CombinedResult addResults(List<Range> ranges) {
            this.ranges.addAll(ranges);
            return this;
        }

        public CombinedResult updateRange(Range updatedRange) {
            carryRange = Optional.of(updatedRange);
            return this;
        }

        public boolean hasResults() {
            return !ranges.isEmpty();
        }

        public boolean rangeProcessed() {
            return !carryRange.isPresent();
        }
    }

    private static class Range {
        private final int start, end;
        private Set<Integer> tags = new HashSet<>();
        private boolean valid;

        public Range(int start, int end) {
            this(Collections.emptySet(), start, end);
        }

        public Range(Set<Integer> tags, int start, int end) {
            this.tags.addAll(tags);
            this.start = start;
            this.end = end;
            this.valid = end > start;
        }

        public Range copy() {
            return new Range(tags, start, end);
        }

        public Range addTag(Integer tag) {
            tags.add(tag);
            return this;
        }

        public Range addTags(Set<Integer> tags) {
            this.tags.addAll(tags);
            return this;
        }

        public boolean disjoint(Range other) {
            return other.start > end || start > other.end;
        }

        public boolean dovetails(Range other) {
            return start < other.start && end < other.end && end > other.start;
        }

        public boolean same(Range other) {
            return start == other.start && end == other.end;
        }

        public boolean contains(Range other) {
            return (start < other.start || start == other.start) &&
                    (end == other.end || end > other.end);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            return start == range.start && end == range.end;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "\n" + start + "-" + end + tags;
        }
    }
}
