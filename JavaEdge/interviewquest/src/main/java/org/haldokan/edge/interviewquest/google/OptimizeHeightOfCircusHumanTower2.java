package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - I solved it using my solution to another Google interview question:
 * GenerifiedLongestSubsequence. Time complexity is O(n^2).
 * <p>
 * The Question: 5_STAR
 * A circus is designing a tower routine consisting of people standing atop one another’s
 * shoulders. For practical and aesthetic reasons, each person must be both shorter and lighter than the person
 * below him or her. Given the heights and weights of each person in the circus, write a method to compute the largest
 * possible number of people in such a tower.
 * <p>
 * EXAMPLE:
 * Input (ht, wt): (65, 100) (70, 150) (56, 90) (75, 190) (60, 95) (68, 110)
 * Output: The longest tower is length 6 and includes from top to bottom:
 * (56, 90) (60,95) (65,100) (68,110) (70,150) (75,190)
 * <p>
 * Created by haytham.aldokanji on 6/11/16.
 */
public class OptimizeHeightOfCircusHumanTower2 {

    public static void main(String[] args) {
        OptimizeHeightOfCircusHumanTower2 driver = new OptimizeHeightOfCircusHumanTower2();

        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
    }

    public Person[] highestTower(Person[] people) {
        // sort by height decreasing
        Arrays.sort(people, (p1, p2) -> p2.height - p1.height);
        GenerifiedLongestSubsequence<Person> longestSubsequence =
                new GenerifiedLongestSubsequence<>((p1, p2) -> p2.weight - p1.weight, Person.class);

        return longestSubsequence.longestSequence(people);
    }

    private void test1() {
        Person[] people = new Person[]{
                new Person(65, 100),
                new Person(70, 150),
                new Person(56, 90),
                new Person(75, 190),
                new Person(60, 95),
                new Person(68, 110)
        };
        Person[] expected = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(68, 110),
                new Person(65, 100),
                new Person(60, 95),
                new Person(56, 90)
        };
        Person[] highestTower = highestTower(people);
        assertThat(highestTower, is(expected));
    }

    private void test2() {
        Person[] people = new Person[]{
                new Person(65, 100),
                new Person(70, 150),
                new Person(56, 97),
                new Person(75, 190),
                new Person(67, 95),
                new Person(68, 110)
        };
        Person[] expected = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(68, 110),
                new Person(65, 100),
                new Person(56, 97)
        };
        Person[] highestTower = highestTower(people);
        assertThat(highestTower, is(expected));
    }

    private void test3() {
        Person[] people = new Person[]{
                new Person(65, 100),
                new Person(70, 150),
                new Person(66, 97),
                new Person(75, 190),
                new Person(67, 95),
                new Person(68, 110)
        };
        Person[] expected1 = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(68, 110),
                new Person(65, 100)
        };
        Person[] expected2 = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(68, 110),
                new Person(67, 95)
        };

        Person[] highestTower = highestTower(people);
        assertThat(highestTower, either(is(expected1)).or(is(expected2)));
    }

    public void test4() {
        Person[] people = new Person[]{
                new Person(65, 200),
                new Person(70, 150),
                new Person(66, 97),
                new Person(75, 190),
                new Person(67, 95),
                new Person(88, 110)
        };
        Person[] expected1 = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(66, 97)
        };
        Person[] expected2 = new Person[]{
                new Person(75, 190),
                new Person(70, 150),
                new Person(67, 95)
        };
        Person[] highestTower = highestTower(people);
        assertThat(highestTower, either(is(expected1)).or(is(expected2)));
    }

    public void test5() {
        Person[] people = new Person[]{
                new Person(65, 185),
                new Person(70, 150),
                new Person(66, 97),
                new Person(55, 190),
                new Person(67, 95),
                new Person(88, 110)
        };
        Person[] expected1 = new Person[]{
                new Person(70, 150),
                new Person(66, 97)
        };
        Person[] expected2 = new Person[]{
                new Person(70, 150),
                new Person(67, 95)
        };

        Person[] highestTower = highestTower(people);
        assertThat(highestTower, either(is(expected1)).or(is(expected2)));
    }

    public void test6() {
        Person[] people = new Person[]{
                new Person(65, 100),
                new Person(70, 85),
                new Person(50, 110)
        };
        Person[] expected1 = new Person[]{new Person(65, 100)};
        Person[] expected2 = new Person[]{new Person(70, 85)};

        Person[] highestTower = highestTower(people);
        assertThat(highestTower, either(is(expected1)).or(is(expected2)));
    }

    private static class Person {
        private final int height, weight;

        public Person(int height, int weight) {
            this.height = height;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            return height == person.height && weight == person.weight;
        }

        @Override
        public int hashCode() {
            int result = height;
            result = 31 * result + weight;
            return result;
        }

        @Override
        public String toString() {
            return "(" + height + ", " + weight + ")";
        }
    }
}
