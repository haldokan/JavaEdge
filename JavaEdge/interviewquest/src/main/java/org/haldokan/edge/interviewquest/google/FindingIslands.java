package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;

/**
 * Not resolved yet.
 * <p>
 * Counting the islands.
 * <p>
 * Given a map N x N, 2-D array
 * 0 - sea
 * X - land
 * <p>
 * Land is connected by 4-Neighbor connections, i.e.: above, down, left and right.
 * <p>
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 0000000000000000000X000000000000000
 * 000000000000000000XXX00000000000000
 * 000XX000000000000000000000000000000
 * 000XXXX0000000000000000000000000000
 * 0000000X000000000000000000000000000
 * 00000000000000000000000000000000000
 * 000000000000000000000X0000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * 00000000000000000000000000000000000
 * <p>
 * Output of this map: 4 (totally 4 islands on the map)
 * Created by haytham.aldokanji on 10/2/15.
 */
public class FindingIslands {
    public static void main(String[] args) {
        int size = 25;

        String[][] topo = new String[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(topo[i], "0");
        }
        topo[3][19] = "X";
        topo[4][18] = "X";
        topo[4][19] = "X";
        topo[4][20] = "X";
        topo[5][3] = "X";
        topo[5][3] = "X";
        topo[6][3] = "X";
        topo[6][4] = "X";
        topo[6][5] = "X";
        topo[6][6] = "X";
        topo[7][7] = "X";
        topo[9][19] = "X";
//        topo[9][20] = "X";
        topo[8][21] = "X";
        topo[9][21] = "X";
        topo[9][22] = "X";
        topo[10][21] = "X";
        topo[10][20] = "X";
        topo[10][19] = "X";

        FindingIslands driver = new FindingIslands();
        driver.outputTopo(topo);
    }

    private void outputTopo(String[][] topo) {
        for (int i = 0; i < topo.length; i++) {
            System.out.println(Arrays.toString(topo[i]));
        }
    }
}
