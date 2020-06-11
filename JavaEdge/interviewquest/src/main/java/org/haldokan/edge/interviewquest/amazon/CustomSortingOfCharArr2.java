package org.haldokan.edge.interviewquest.amazon;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * My solution to an Amazon interview question. Resolved in O(nlogn) using sort.
 * Not resolved in O(n) - seems to be about the dexterity in which one can track different indexes
 * The Question: 3_STAR
 * A string contains a-z, A-Z and spaces. Sort the string so that all lower cases are at the beginning, spaces in
 * the middle and upper cases at the end. Original order among lower and upper cases needs to remain the same.
 * For example: a cBd LkmY becomes acdkm BLY. Is there a way in O(n) without extra space?
 */
public class CustomSortingOfCharArr2 {
    static Map<Character, Integer> charIndex = new HashMap<>();

    public static void main(String[] args) {
        char[] arr = new char[]{'a', ' ', 'c', 'B', 'd', ' ', 'L', 'k', 'm', 'Y'};
        sort(arr);
        System.out.printf("'%s'%n", String.valueOf(arr));
    }


    static void sort(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            charIndex.put(arr[i], i);
        }
        doSort(arr, 0, arr.length);
    }

    static void doSort(char[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int pivot = (start + end) / 2;
        for (int i = 0; i < pivot; i++) {
            if (swap(arr[i], arr[pivot])) {
                char tmp = arr[i];
                arr[i] = arr[pivot];
                arr[pivot] = tmp;
            }
        }
        doSort(arr, start, pivot);
        doSort(arr, pivot + 1, end);
    }

    static boolean isUpper(char chr) {
        return chr >= 'A' && chr <= 'Z';
    }

    static boolean isLower(char chr) {
        return chr >= 'a' && chr <= 'z';
    }

    static boolean swap(char chr1, char chr2) {
        if (chr1 == ' ' && chr2 == ' ') {
            return false;
        }
        if (isUpper(chr1) && (isLower(chr2) || chr2 == ' ')) {
            return true;
        }
        if (chr1 == ' ' && isLower(chr2)) {
            return true;
        }
        if (isUpper(chr1) && isUpper(chr2)) {
            return charIndex.get(chr1) > charIndex.get(chr2);
        }
        if (isLower(chr1) && isLower(chr2)) {
            return charIndex.get(chr1) > charIndex.get(chr2);
        }
        return false;
    }
}
