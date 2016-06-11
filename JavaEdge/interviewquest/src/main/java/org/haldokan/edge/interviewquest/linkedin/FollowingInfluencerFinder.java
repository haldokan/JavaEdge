package org.haldokan.edge.interviewquest.linkedin;

import java.util.Arrays;
import java.util.Random;

/**
 * My solution to a LinkedIn interview Question. It handles a more general case of non-square matrixes
 * The Question: 3_STAR
 * Given a matrix of following between N LinkedIn users (with ids from 0 to N-1): followingMatrix[i][j] == true iff user
 * i is following user j thus followingMatrix[i][j] doesn't imply followingMatrix[j][i]. Let's also agree that
 * followingMatrix[i][i] == false
 * <p>
 * Influencer is a user who is:
 * <p>
 * - followed by everyone else and
 * <p>
 * - not following anyone himself
 * <p>
 * This method should find an Influencer by a given matrix of following, or return -1 if there is no Influencer in this
 * group.
 */
public class FollowingInfluencerFinder {
    private static final Random rand = new Random();

    public static void main(String[] args) {
        FollowingInfluencerFinder finder = new FollowingInfluencerFinder();
        for (int i = 0; i < 10; i++) {
            boolean[][] fmatrix = finder.createFollowingMatrix(5, 8, true);
            // finder.printMatrix(fmatrix);
            System.out.println("influencer " + finder.findInfluencer(fmatrix));

            fmatrix = finder.createFollowingMatrix(8, 5, true);
            System.out.println("influencer " + finder.findInfluencer(fmatrix));

            fmatrix = finder.createFollowingMatrix(1, 5, true);
            System.out.println("influencer " + finder.findInfluencer(fmatrix));

            fmatrix = finder.createFollowingMatrix(5, 1, true);
            System.out.println("influencer " + finder.findInfluencer(fmatrix));

            // there is a slight probability (calculate it!) that the random matrix creation yield one with an
            // influencer
            boolean[][] fmatrix2 = finder.createFollowingMatrix(8, 5, false);
            System.out.println("influencer " + finder.findInfluencer(fmatrix2));
        }
    }

    private boolean[][] createFollowingMatrix(int numRows, int numCols, boolean hasInfluencer) {
        boolean[][] matrix = new boolean[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = rand.nextBoolean();
            }
        }
        if (hasInfluencer) {
            // make sure row chosen is in a square of numRows X numCols
            int row = rand.nextInt(numRows) % numCols;
            for (int j = 0; j < numCols; j++) {
                matrix[row][j] = false;
            }

            for (int i = 0; i < numRows; i++) {
                matrix[i][row] = true;
            }
            matrix[row][row] = false;
        }
        return matrix;
    }

    public int findInfluencer(boolean[][] fmatrix) {
        int influencerRowIndex = findCandidateInfluencerIndex(fmatrix);
        if (influencerRowIndex == -1)
            return -1;
        boolean[] followers = getInfluencerFollowers(fmatrix, influencerRowIndex);
        followers[influencerRowIndex] = true;

        if (followedByAll(followers, followers.length - 1))
            return influencerRowIndex;
        return -1;

    }

    private int findCandidateInfluencerIndex(boolean[][] fmatrix) {
        int influencerRowIndex = -1;
        int numCols = fmatrix[0].length;
        for (int i = 0; i < fmatrix.length; i++) {
            if (followsNone(fmatrix[i], numCols - 1)) {
                influencerRowIndex = i;
                break;
            }
        }
        // guard against candidate influencer row larger the max num of columns
        if (influencerRowIndex >= numCols)
            return -1;
        return influencerRowIndex;
    }

    private boolean[] getInfluencerFollowers(boolean[][] fmatrix, int influencerRowIndex) {
        boolean[] followers = new boolean[fmatrix.length];
        for (int i = 0; i < fmatrix.length; i++) {
            followers[i] = fmatrix[i][influencerRowIndex];
        }
        return followers;
    }

    private boolean followsNone(boolean[] arr, int index) {
        if (index == 0)
            return !arr[0];
        // note the we decrement b4 we call recursively; having (index--) leads to stack overflow
        return !arr[index] && followsNone(arr, --index);
    }

    private boolean followedByAll(boolean[] arr, int index) {
        if (index == 0)
            return arr[0];
        return arr[index] && followedByAll(arr, --index);
    }

    public void printMatrix(boolean[][] matrix) {
        for (boolean[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}
