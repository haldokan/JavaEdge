package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * My solution to an Amazon interview question - solved nicely using min heap. I don't support here listing users with
 * the same number of logins as one group. For that look at the other version I provide for this question:
 * TopKUsersBasedOnNumOfLogins2
 * <p>
 * Given a file (which can be considered as a String with comma delimiter for the complexity of the question)
 * of usernames and a value k, find top k usernames (with number of logins) who logged into the system the most.
 * <p>
 * For example -
 * Input:
 * User (String) = user1, user4, user2, user1, user3, user1, user2, user3
 * k (int) = 2
 * <p>
 * Output:
 * user1 (3)
 * user2 (2)
 * user3 (2)
 * <p>
 * - Both user2 and user3 should be included since both has same number of logins
 * <p>
 * Write a java method to find the output with best time and space complexity.
 * Created by haytham.aldokanji on 7/5/16.
 */
public class TopKUsersBasedOnNumOfLogins2 {

    public static void main(String[] args) {
        TopKUsersBasedOnNumOfLogins2 driver = new TopKUsersBasedOnNumOfLogins2();

        List<String> logins =
                Lists.newArrayList("user1", "user2", "user1", "user3", "user3", "user1", "user4");
        UserLoginCount[] topK = driver.topK(logins, 2);
        assertThat(topK, is(new UserLoginCount[]{new UserLoginCount("user1", 3), new UserLoginCount("user3", 2)}));
    }

    public UserLoginCount[] topK(List<String> logins, int size) {
        Queue<UserLoginCount> minHeap = buildMinHeap(logins, size);

        int effectiveK = Math.min(size, minHeap.size());
        UserLoginCount[] topK = new UserLoginCount[effectiveK];

        IntStream.range(0, effectiveK).forEach(i -> {
            topK[effectiveK - i - 1] = minHeap.remove();
        });
        return topK;
    }

    private Queue<UserLoginCount> buildMinHeap(List<String> logins, int size) {
        Map<String, Integer> loginsPerUser = logins.stream()
                .collect(Collectors.groupingBy(login -> login, Collectors.summingInt(v -> 1)));

        Queue<UserLoginCount> minHeap =
                new PriorityQueue<>((e1, e2) -> e1.numberOfLogins - e2.numberOfLogins);

        for (Map.Entry<String, Integer> entry : loginsPerUser.entrySet()) {
            UserLoginCount loginCount = new UserLoginCount(entry.getKey(), entry.getValue());
            if (minHeap.size() < size) {
                minHeap.add(loginCount);
            } else if (entry.getValue() > minHeap.peek().numberOfLogins) {
                minHeap.remove();
                minHeap.add(loginCount);
            }
        }
        return minHeap;
    }

    private static class UserLoginCount {
        private final String user;
        private final int numberOfLogins;

        public UserLoginCount(String user, int numberOfLogins) {
            this.user = user;
            this.numberOfLogins = numberOfLogins;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserLoginCount that = (UserLoginCount) o;

            return numberOfLogins == that.numberOfLogins && !(user != null ? !user.equals(that.user) : that.user != null);
        }

        @Override
        public int hashCode() {
            int result = user != null ? user.hashCode() : 0;
            result = 31 * result + numberOfLogins;
            return result;
        }

        @Override
        public String toString() {
            return user + ": " + numberOfLogins;
        }
    }
}
