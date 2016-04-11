package org.haldokan.edge.interviewquest.amazon;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * My solution to an Amazon interview question. Resolved in O(nlogn) using sort.
 * Not resolved in O(n) - seems to be about the dexterity in which one can track different indexes
 * <p>
 * A string contains a-z, A-Z and spaces. Sort the string so that all lower cases are at the beginning, spaces in
 * the middle and upper cases at the end. Original order among lower and upper cases needs to remain the same.
 * For example: a cBd LkmY becomes acdkm BLY. Is there a way in O(n) without extra space?
 */
public class StringSorting_On {

    public static void main(String[] args) {
//        sort_On("cBd LkmY");
        System.out.println(sort_Ologn("tBOheN    DseS"));
    }

    private static String sort_Ologn(String str) {
        return Stream.of(str.split(""))
                .map(v -> v.charAt(0))
                .sorted((v1, v2) -> {
                    if (Character.isAlphabetic(v1) && Character.isAlphabetic(v2)) {
                        if (Character.isLowerCase(v1) && Character.isLowerCase(v2)) {
                            return 0;
                        } else if (Character.isUpperCase(v1) && Character.isUpperCase(v2)) {
                            return 0;
                        } else {
                            return -1 * v1.compareTo(v2);
                        }
                    } else if (!(Character.isAlphabetic(v1) || Character.isAlphabetic(v2))) {
                        return 0;
                    } else if (Character.isUpperCase(v1)) {
                        return 1;
                    } else if (Character.isUpperCase(v2)) {
                        return -1;
                    } else if (Character.isLowerCase(v1)) {
                        return -1;
                    } else if (Character.isLowerCase(v2)) {
                        return 1;
                    }
                    throw new RuntimeException("Missed combination: " + v1 + "/" + v2);
                }).map(Object::toString).collect(Collectors.joining(""));
    }

    // TODO NEED more work
    private static String sort_On(String str) {
        Character[] chars = Stream.of(str.split(""))
                .map(v -> v.charAt(0))
                .collect(Collectors.toList())
                .toArray(new Character[str.length()]);

        int capsStartNdx = -1;
        int capsEndNdx = -1;
        int lowsStartNdx = -1;
        int lowsEndNdx = -1;
        int spaceStartNdx = -1;
        int spaceEndNdx = -1;

        int ndx = 0;
        while (ndx < chars.length) {
            Character chr = chars[ndx];

            if (Character.isUpperCase(chr)) {
                if (capsStartNdx == -1) {
                    capsStartNdx = ndx;
                }
                while (capsEndNdx < chars.length && Character.isUpperCase(chr)) {
                    capsEndNdx++;
                }

            }
        }
        return null;
    }
}
