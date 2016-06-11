package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * My solution to a Google interview question - a nice twist on n-arry tree BFS
 *
 * The Question: 4_STAR
 *
 * Print combinations of strings from List of List of String
 * <p>
 * Example input: [[quick, slow], [brown, red], [fox, dog]]
 * <p>
 * Output:
 * quick brown fox
 * quick brown dog
 * quick red fox
 * quick red dog
 * slow brown fox
 * slow brown dog
 * slow red fox
 * slow red dog
 * <p>
 * Created by haytham.aldokanji on 6/4/16.
 */
public class PrintCombinationsFromListOfListOfStrings {

    public static void main(String[] args) {
        PrintCombinationsFromListOfListOfStrings driver = new PrintCombinationsFromListOfListOfStrings();

        driver.test();
    }

    public void print(List<List<String>> listOfLists) {
        List<String> firstList = listOfLists.get(0);
        if (listOfLists.size() == 1) {
            System.out.printf("%s%n", firstList.stream().collect(Collectors.joining(" ")));
        } else {
            Deque<String> evalDeck = new ArrayDeque<>();
            firstList.stream().forEach(evalDeck::add);

            while (!evalDeck.isEmpty()) {
                String item = evalDeck.remove();
                // I am assuming that items consist of a single string, if not it is easy to add to the deck an object
                //{string, listIndex)} instead of just the string value
                int nextListIndex = item.split(" ").length;
                if (nextListIndex >= listOfLists.size()) {
                    System.out.printf("%s%n", item);
                } else {
                    List<String> children = listOfLists.get(nextListIndex);
                    evalDeck.addAll(children.stream().map(child -> item + " " + child).collect(Collectors.toList()));
                }
            }
        }

    }

    private void test() {
        List<List<String>> listOfLists = new ArrayList<>();

        List<String> list1 = Lists.newArrayList("quick", "slow");
        List<String> list2 = Lists.newArrayList("brown", "red");
        List<String> list3 = Lists.newArrayList("fox", "dog", "pig");

        listOfLists.add(list1);
        listOfLists.add(list2);
        listOfLists.add(list3);

        print(listOfLists);
    }
}
