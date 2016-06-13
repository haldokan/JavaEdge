package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - Space O(n), time complexity O(n^3). I present another solution using
 * dynamic programming here: GenerifiedLongestSubsequence. Time complexity is reduced to O(n^2).
 *
 * <p>
 * The Question: 3_STAR
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
public class OptimizeHeightOfCircusHumanTower1 {

    public static void main(String[] args) {
        OptimizeHeightOfCircusHumanTower1 driver = new OptimizeHeightOfCircusHumanTower1();

        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
    }

    public Optional<List<int[]>> highestTower(int[][] people) {
        List<List<int[]>> towers = new ArrayList<>();

        for (int[] person : people) {
            insertInTower(towers, person);
        }
        List<int[]> highest = null;
        for (List<int[]> tower : towers) {
            if (highest == null || tower.size() > highest.size()) {
                highest = tower;
            }
        }
        return Optional.ofNullable(highest);
    }

    private void insertInTower(List<List<int[]>> towers, int[] newPerson) {
        boolean added = false;
        for (List<int[]> tower : towers) {
            int insertIndex1 = getInsertIndex1(tower, newPerson);
            int insertIndex2 = getInsertIndex2(tower, newPerson);

            if (insertIndex1 == insertIndex2) {
                tower.add(insertIndex1, newPerson);
                added = true;
            }
        }
        if (!added) {
            towers.add(Lists.newLinkedList(Arrays.asList(newPerson)));
        }
    }

    private int getInsertIndex1(List<int[]> tower, int[] newPerson) {
        int insertIndex1 = -1;
        for (int i = tower.size() - 1; i >= 0; i--) {
            int[] towerPerson = tower.get(i);
            if (newPerson[0] < towerPerson[0] && newPerson[1] < towerPerson[1]) {
                insertIndex1 = i + 1;
                break;
            }
        }
        if (insertIndex1 == -1) {
            int[] bottomOfTowerPerson = tower.get(0);
            if (newPerson[0] > bottomOfTowerPerson[0] && newPerson[1] > bottomOfTowerPerson[1]) {
                insertIndex1 = 0;
            }
        }
        return insertIndex1;
    }

    private int getInsertIndex2(List<int[]> tower, int[] newPerson) {
        int insertIndex2 = -2;
        for (int i = 0; i < tower.size(); i++) {
            int[] towerPerson = tower.get(i);
            if (newPerson[0] > towerPerson[0] && newPerson[1] > towerPerson[1]) {
                insertIndex2 = i;
                break;
            }
        }
        if (insertIndex2 == -2) {
            int[] topOfTowerPerson = tower.get(tower.size() - 1);
            if (newPerson[0] < topOfTowerPerson[0] && newPerson[1] < topOfTowerPerson[1]) {
                insertIndex2 = tower.size();
            }
        }
        return insertIndex2;
    }

    private void test1() {
        int[][] people = new int[][]{
                new int[]{65, 100},
                new int[]{70, 150},
                new int[]{56, 90},
                new int[]{75, 190},
                new int[]{60, 95},
                new int[]{68, 110}
        };
        List<int[]> expected = Lists.newArrayList(
                new int[]{75, 190},
                new int[]{70, 150},
                new int[]{68, 110},
                new int[]{65, 100},
                new int[]{60, 95},
                new int[]{56, 90}
        );
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));
    }

    private void test2() {
        int[][] people = new int[][]{
                new int[]{65, 100},
                new int[]{70, 150},
                new int[]{56, 97},
                new int[]{75, 190},
                new int[]{67, 95},
                new int[]{68, 110}
        };
        List<int[]> expected = Lists.newArrayList(
                new int[]{75, 190},
                new int[]{70, 150},
                new int[]{68, 110},
                new int[]{65, 100},
                new int[]{56, 97}
        );
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));
    }

    private void test3() {
        int[][] people = new int[][]{
                new int[]{65, 100},
                new int[]{70, 150},
                new int[]{66, 97},
                new int[]{75, 190},
                new int[]{67, 95},
                new int[]{68, 110}
        };
        List<int[]> expected = Lists.newArrayList(
                new int[]{75, 190},
                new int[]{70, 150},
                new int[]{68, 110},
                new int[]{65, 100}
        );
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));
    }

    public void test4() {
        int[][] people = new int[][]{
                new int[]{65, 200},
                new int[]{70, 150},
                new int[]{66, 97},
                new int[]{75, 190},
                new int[]{67, 95},
                new int[]{88, 110}
        };
        List<int[]> expected = Lists.newArrayList(
                new int[]{75, 190},
                new int[]{70, 150},
                new int[]{66, 97}
        );
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));

    }

    public void test5() {
        int[][] people = new int[][]{
                new int[]{65, 185},
                new int[]{70, 150},
                new int[]{66, 97},
                new int[]{55, 190},
                new int[]{67, 95},
                new int[]{88, 110}
        };
        List<int[]> expected = Lists.newArrayList(
                new int[]{70, 150},
                new int[]{66, 97}
        );
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));

    }

    public void test6() {
        int[][] people = new int[][]{
                new int[]{65, 100},
                new int[]{70, 85},
                new int[]{50, 110},
        };
        List<int[]> expected = Lists.newArrayList(new int[]{65, 100}); // could be any one of them
        expected.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        List<int[]> highestTower = highestTower(people).get();
        highestTower.stream().forEach(person -> System.out.printf("%s", Arrays.toString(person)));
        System.out.printf("%n");

        assertThat(highestTower.size(), is(expected.size()));
        IntStream.range(0, highestTower.size()).forEach(index ->
                assertThat(highestTower.get(index), is(expected.get(index))));

    }

}
