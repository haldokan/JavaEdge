package org.haldokan.edge.interviewquest.google;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - solution works for non square arrays.
 *
 * The Question: 5_STAR
 * <p>
 * Given a map N x N, 2-D array
 * 0 - sea
 * X - land
 * <p>
 * Count the islands. Land is connected by one of the 4-Neighbor connections, i.e.: above, down, left and right.
 * The map below should have 6 islands
 * <p>
 * X0000X0000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * X0000XXXX0000000000X00000
 * 00000000X000000000XXX0000
 * 000X0000X0000000000000000
 * 000XXXX0X0000000000000000
 * 000000XXX0000000000000000
 * 000000000000000000000X000
 * 0000000000000000000X0XX00
 * 0000000000000000000XXX000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * 0000000000000000000000000
 * <p>
 * Created by haytham.aldokanji on 04/29/16.
 */
public class FindingIslands {
    public static void main(String[] args) {
        FindingIslands driver = new FindingIslands();

        char[][] topography = driver.makeTopography();
        driver.printTopography(topography);

        List<Set<Land>> islands = driver.islands(topography);
        driver.assertResults(islands);
    }

    public List<Set<Land>> islands(char[][] topography) {
        List<Set<Land>> islands = new ArrayList<>();

        for (int i = 0; i < topography.length; i++) {
            char[] row = topography[i];
            for (int j = 0; j < row.length; j++) {
                char chr = row[j];

                if (isLand(chr)) {
                    Land land = new Land(i, j);
                    boolean attached = false;
                    if (land.x > 0 && isLand(topography[land.x - 1][land.y])) {
                        attache(islands, land, new Land(land.x - 1, land.y));
                        attached = true;
                    }
                    if ((land.y > 0 && isLand(topography[land.x][land.y - 1]))) {
                        attache(islands, land, new Land(land.x, land.y - 1));
                        attached = true;
                    }
                    if (!attached) {
                        islands.add(makeIsland(land));
                    }
                }
            }
        }
        return islands;
    }

    private void attache(List<Set<Land>> islands, Land newLand, Land neighbor) {
        Set<Land> islandForNewLand = getIslandForLand(islands, newLand);
        Set<Land> islandForNeighbor = getIslandForLand(islands, neighbor);

        if (!islandForNewLand.equals(islandForNeighbor)) {
            islandForNeighbor.addAll(islandForNewLand);
            islands.remove(islandForNewLand);
        }
    }

    private Set<Land> getIslandForLand(List<Set<Land>> islands, Land land) {
        for (Set<Land> island : islands) {
            if (island.contains(land)) {
                return island;
            }
        }
        return makeIsland(land);
    }

    private Set<Land> makeIsland(Land... lands) {
        Set<Land> newIsland = new HashSet<>();
        Arrays.stream(lands).forEach(newIsland::add);
        return newIsland;
    }

    private boolean isLand(char chr) {
        return chr == 'X';
    }

    private void assertResults(List<Set<Land>> islands) {
        String expectedResult = "[0,0][0,5][3,0][3,19][4,18][4,19][4,20][3,5][3,6][3,7][3,8][4,8][5,3][5,8][6,3]" +
                "[6,4][6,5][6,6][6,8][7,6][7,7][7,8][8,21][9,19][9,21][9,22][10,19][10,20][10,21]";

        StringBuilder result = new StringBuilder();
        islands.stream().forEach(island -> {
            island.stream()
                    .sorted((land1, land2) -> land1.x == land2.x ? land1.y - land2.y : land1.x - land2.x)
                    .forEach(land -> {
                        result.append(land.toString());
                        System.out.print(land);
                    });
            System.out.println();
        });
        assertThat(result.toString(), is(expectedResult));
    }

    private void printTopography(char[][] topography) {
        Arrays.stream(topography).map(Arrays::toString).forEach(System.out::println);
    }

    private char[][] makeTopography() {
        int size = 25;

        char[][] topography = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(topography[i], '0');
        }
        topography[0][0] = 'X';
        topography[0][5] = 'X';
        topography[3][0] = 'X';
        topography[3][5] = 'X';
        topography[3][19] = 'X';
        topography[3][6] = 'X';
        topography[3][7] = 'X';
        topography[3][8] = 'X';
        topography[4][8] = 'X';
        topography[5][8] = 'X';
        topography[6][8] = 'X';
        topography[7][6] = 'X';
        topography[7][8] = 'X';
        topography[4][18] = 'X';
        topography[4][19] = 'X';
        topography[4][20] = 'X';
        topography[5][3] = 'X';
        topography[5][3] = 'X';
        topography[6][3] = 'X';
        topography[6][4] = 'X';
        topography[6][5] = 'X';
        topography[6][6] = 'X';
        topography[7][7] = 'X';
        topography[9][19] = 'X';
        topography[8][21] = 'X';
        topography[9][21] = 'X';
        topography[9][22] = 'X';
        topography[10][21] = 'X';
        topography[10][20] = 'X';
        topography[10][19] = 'X';

        return topography;
    }

    private static class Land {
        private final int x, y;

        public Land(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Land land = (Land) o;

            if (x != land.x) return false;
            return y == land.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }
    }
}
