package org.haldokan.edge.interviewquest.bloomberg;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * My solution to a Bloomberg interview question
 * The Question: 3_STAR
 * streaming byte data coming which can be digits or or letters
 * you have to sort both the digits and letters with their index intact.
 * for e.g you have c6b2e4a
 * your sorted array should be a2b4c6e
 */
public class SortingMixedTypesWhileKeepingIndexes {

    public static void main(String[] args) {
        SortingMixedTypesWhileKeepingIndexes driver = new SortingMixedTypesWhileKeepingIndexes();
        System.out.println(driver.sort("c6b2e4a"));
    }

    public String sort(String str) {
        Character[] arr = Arrays.stream(str.split("")).map(s -> s.charAt(0)).toArray(Character[]::new);
        int[] indexes = new int[arr.length];

        int numChars = 0;
        int numInts = 0;

        for (int i = 0; i < arr.length; i++) {
            if (Character.isDigit(arr[i])) {
                numInts++;
                indexes[indexes.length - numInts] = i;
            } else {
                indexes[numChars++] = i;
            }
        }

        Arrays.sort(arr);
        // I think there is no way to modify the arr in-situ so we create a new one
        Character[] arrPartitionSorted = new Character[arr.length];
        numChars = 0;
        numInts = 0;

        for (char c : arr) {
            if (Character.isDigit(c)) {
                numInts++;
                arrPartitionSorted[indexes[indexes.length - numInts]] = c;
            } else {
                arrPartitionSorted[indexes[numChars++]] = c;
            }
        }

        return Arrays.stream(arrPartitionSorted).map(String::valueOf).collect(Collectors.joining());
    }
}
