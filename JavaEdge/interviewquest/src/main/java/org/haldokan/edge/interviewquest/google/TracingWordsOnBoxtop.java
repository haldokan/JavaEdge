package org.haldokan.edge.interviewquest.google;

/**
 * My solution to a Google interview question
 * The Question: 3_STAR
 * <p>
 * Given a set top box:
 * a, b, c, d, e,
 * f, g, h, i, j,
 * k, l, m, n, o
 * p, q, r, s, t
 * u, v, w, x, y
 * z
 * <p>
 * Write code to give the character sequence given a word
 * For example, if the word is "CON", the function will print this:
 * <p>
 * Right//now we're at B
 * Right//now we're at C
 * OK//to select C
 * Down
 * DOwn
 * Right
 * Right
 * OK//to select O
 * Left//now at N
 * OK//to select N
 * <p>
 * Created by haytham.aldokanji on 6/29/16.
 */
public class TracingWordsOnBoxtop {

    private static final char[][] boxtop = new char[][]{
            {'a', 'b', 'c', 'd', 'e'},
            {'f', 'g', 'h', 'i', 'j'},
            {'k', 'l', 'm', 'n', 'o',},
            {'p', 'q', 'r', 's', 't'},
            {'u', 'v', 'w', 'x', 'y'},
            {'z'}
    };

    public static void main(String[] args) {
        TracingWordsOnBoxtop driver = new TracingWordsOnBoxtop();
        driver.test();
    }

    public void trace(String word) {
        char[] chars = word.toCharArray();
        int activeRow = 0;
        int activeCol = 0;

        for (char chr : chars) {
            int order = chr - 'a';
            int charRow = order / 5;
            int charCol = order % 5;

            while (activeCol < charCol) {
                int row = activeRow - activeRow / 5;
                if (row != activeRow) {
                    System.out.printf("U: %s%n", boxtop[row][activeCol]);
                    activeRow = row;
                }
                activeCol++;
                System.out.printf("R: %s%n", boxtop[row][activeCol]);
            }

            while (activeCol > charCol) {
                activeCol--;
                System.out.printf("L: %s%n", boxtop[activeRow][activeCol]);
            }

            while (activeRow < charRow) {
                activeRow++;
                System.out.printf("D: %s%n", boxtop[activeRow][activeCol]);
            }

            while (activeRow > charRow) {
                activeRow--;
                System.out.printf("U: %s%n", boxtop[activeRow][activeCol]);
            }

            if (boxtop[activeRow][activeCol] != chr) {
                throw new IllegalStateException("char not found: " + chr);
            }
            System.out.printf("select: %s@(%d, %d)%n", chr, activeRow, activeCol);
        }
    }

    private void test() {
        trace("cloud");
        System.out.println("-----------------");
        trace("zebra");
        System.out.println("-----------------");
        trace("aztec");
        System.out.println("-----------------");
        trace("lozenges");
        System.out.println("-----------------");
        trace("deer");
    }
}
