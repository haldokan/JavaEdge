package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * My solution to a Google interview question - a question that doesn't cause brain pain for a change!
 *
 * The Question: 4_STAR
 *
 * Write a method that combines an array of iterators into a new one, such that, e.g. for input [A B C] where:
 * A.next() returns a1, a2, respectively;
 * B.next() returns b1;
 * C.next() returns c1, c2, c3, respectively;
 * <p>
 * The new iterator will return elements in this order: a1 b1 c1 a2 c2 c3.
 * Created by haytham.aldokanji on 5/20/16.
 */
public class CombiningIterators {
    private static final int NO_INDEX = -1;

    public static void main(String[] args) {
        CombiningIterators driver = new CombiningIterators();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
    }

    private void test1() {
        Iterator<String> it1 = Lists.newArrayList("a1", "a2").iterator();
        Iterator<String> it2 = Lists.newArrayList("b1").iterator();
        Iterator<String> it3 = Lists.<String>newArrayList("c1", "c2", "c3").iterator();

        CompoIterator<String> compoIterator = CompoIterator.create(new Iterator[]{it1, it2, it3});

        while (compoIterator.hasNext()) {
            assertThat(compoIterator.next(), is("a1"));
            assertThat(compoIterator.next(), is("b1"));
            assertThat(compoIterator.next(), is("c1"));
            assertThat(compoIterator.next(), is("a2"));
            assertThat(compoIterator.next(), is("c2"));
            assertThat(compoIterator.next(), is("c3"));
            try {
                compoIterator.next();
                fail("Exception was expected");
            } catch (NoSuchElementException e) {
                System.out.println("Expected testing exception: " + e);
                break;
            }
        }
    }

    private void test2() {
        Iterator<String> it1 = new ArrayList<String>().iterator();
        Iterator<String> it2 = new ArrayList<String>().iterator();
        Iterator<String> it3 = Lists.<String>newArrayList("c1").iterator();

        CompoIterator<String> compoIterator = CompoIterator.create(new Iterator[]{it1, it2, it3});

        while (compoIterator.hasNext()) {
            assertThat(compoIterator.next(), is("c1"));
            try {
                compoIterator.next();
                fail("Exception was expected");
            } catch (NoSuchElementException e) {
                System.out.println("Expected testing exception: " + e);
                break;
            }
        }
    }

    private void test3() {
        Iterator<String> it1 = Lists.newArrayList("a1", "a2").iterator();

        CompoIterator<String> compoIterator = CompoIterator.create(new Iterator[]{it1});

        while (compoIterator.hasNext()) {
            assertThat(compoIterator.next(), is("a1"));
            assertThat(compoIterator.next(), is("a2"));
            try {
                compoIterator.next();
                fail("Exception was expected");
            } catch (NoSuchElementException e) {
                System.out.println("Expected testing exception: " + e);
                break;
            }
        }
    }

    private void test4() {
        Iterator<String> it1 = new ArrayList<String>().iterator();
        Iterator<String> it2 = new ArrayList<String>().iterator();
        Iterator<String> it3 = new ArrayList<String>().iterator();

        CompoIterator<String> compoIterator = CompoIterator.create(new Iterator[]{it1, it2, it3});

        while (compoIterator.hasNext()) {
            fail("Iterators have no elements. It should not iterate at all");
        }
    }

    private static class CompoIterator<T> {
        private final Iterator<T>[] iterators;
        private int activeIndex;

        private CompoIterator(Iterator<T>[] iterators) {
            this.iterators = iterators;
            this.activeIndex = -1;
        }

        public static <T> CompoIterator<T> create(Iterator<T>[] iterators) {
            return new CompoIterator<>(iterators);
        }

        public boolean hasNext() {
            return nextIndex() != NO_INDEX;
        }

        public T next() {
            activeIndex = nextIndex();
            if (activeIndex == NO_INDEX) {
                throw new NoSuchElementException();
            }
            return iterators[activeIndex].next();
        }

        private int nextIndex() {
            int nextIndex = (activeIndex + 1) % iterators.length;
            int spotIndex = nextIndex;

            while (!iterators[nextIndex].hasNext()) {
                nextIndex = (nextIndex + 1) % iterators.length;
                if (nextIndex == spotIndex) {
                    return NO_INDEX;
                }
            }
            return nextIndex;
        }
    }
}
