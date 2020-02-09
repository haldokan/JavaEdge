package org.haldokan.edge.interviewquest.facebook;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Given unsorted array, sort it in such a way that the first
 * element is the largest value, the second element is the smallest,
 * the third element is the second largest element and so on.
 * [2, 4, 3, 5, 1] -> [5, 1, 4, 2, 3]
 * can you do it without using extra space
 *
 * public void sortAlternate(int[] nums){}
 *
 * Created by haldokanji on 4/16/17.
 */
public class AlternateSort {
    public static void main(String[] args) {

    }

    public void sortAlternate(int[] numbers) {
        // 9 8 7 6 5 4 3 2 1 0
        //   |
        // 9 0 7 6 5 4 3 2 1 8
        //     |
        // 9 0 8 6 5 4 3 2 1 7
        //       |
        // 9 0 8 1 5 4 3 2 6 7
        //         |
        // 9 0 8 1 7 4 3 2 6 5
        //           |
        // 9 0 8 1 7 2 3 4 6 5
        //             |
        // 9 0 8 1 7 2
    }

    public static <E extends Comparable<E>> void mergeArrays(E[] d, int low, int mid, int high) {
        System.out.printf("merge %d-%d\n", low, high);
//        System.out.println("before:" + Arrays.toString(d));
        List<E> buf1 = copy(d, low, mid);
        List<E> buf2 = copy(d, mid, high);
        int x = low;
        while (!(buf1.isEmpty() || buf2.isEmpty())) {
            if (buf1.get(0).compareTo(buf2.get(0)) < 0)
                d[x++] = buf1.remove(0);
            else
                d[x++] = buf2.remove(0);

        }
        for (E e : buf1)
            d[x++] = e;
        for (E e : buf2)
            d[x++] = e;
//        System.out.println("after :" + Arrays.toString(d));
    }

    private static <E> List<E> copy(E[] d, int start, int end) {
        return new LinkedList<E>(Arrays.asList(d).subList(start, end));
    }
}
