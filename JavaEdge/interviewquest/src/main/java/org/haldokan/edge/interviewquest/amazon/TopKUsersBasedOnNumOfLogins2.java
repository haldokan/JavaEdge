package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * My solution to an Amazon interview question - using a combination b/w a minheap and a hashmap we are able to get
 * top k users. Heap is used to trim the hashmap of entries that fall off the heap. Heap has the number of logins while
 * hashmap has mappings b/w number of logins and users.
 * The Question: 4_STAR
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
 * NOTE that both user2 and user3 should be included since both have the same number of logins
 * <p>
 * Write a java method to find the output with best time and space complexity.
 *
 * Created by haytham.aldokanji on 7/5/16.
 */

public class TopKUsersBasedOnNumOfLogins2 {

    public static void main(String[] args) {
        TopKUsersBasedOnNumOfLogins2 driver = new TopKUsersBasedOnNumOfLogins2();

        List<String> logins = Lists.newArrayList(
                "user1", "user2", "user1", "user3",
                "user3", "user1", "user4", "user3",
                "user2", "user5", "user4", "user6",
                "user6", "user6", "user6"
        );
        List<UserLoginCount> topK = driver.topK(logins, 2);
        // note that despite that we are asking for top 2 we are getting 3 users
        System.out.printf("%s%n", topK);
        assertThat(topK.toArray(), is(new UserLoginCount[]{
                new UserLoginCount("user6", 4),
                new UserLoginCount("user3", 3),
                new UserLoginCount("user1", 3)
        }));
    }

    private List<UserLoginCount> topK(List<String> logins, int size) {
        Map<String, Integer> loginsPerUser = logins.stream()
                .collect(Collectors.groupingBy(login -> login, Collectors.summingInt(v -> 1)));

        Queue<Integer> minHeap = new PriorityQueue<>((e1, e2) -> e1 - e2);
        Multimap<Integer, String> usersPerLogin = HashMultimap.create();

        for (Map.Entry<String, Integer> entry : loginsPerUser.entrySet()) {
            if (minHeap.size() < size) {
                boolean newKey = usersPerLogin.put(entry.getValue(), entry.getKey());
                if (newKey) {
                    minHeap.add(entry.getValue());
                }
            } else if (usersPerLogin.containsKey(entry.getValue())) {
                usersPerLogin.put(entry.getValue(), entry.getKey());
            } else if (entry.getValue() > minHeap.peek()) {
                Integer count = minHeap.remove();
                minHeap.add(entry.getValue());

                //trim the map since count has fallen off the heap
                usersPerLogin.removeAll(count);
                usersPerLogin.put(entry.getValue(), entry.getKey());
            }
        }
        // put result into sorted list using the heap as a sorter for map
        List<UserLoginCount> loginCounts = new LinkedList<>();
        while (!minHeap.isEmpty()) {
            Integer count = minHeap.remove();
            usersPerLogin.get(count)
                    .stream()
                    .forEach(user -> loginCounts.add(0, new UserLoginCount(user, count)));
        }
        return loginCounts;
    }

    private static class UserLoginCount {
        private final String user;
        private final Integer numberOfLogins;

        public UserLoginCount(String user, int numberOfLogins) {
            this.user = user;
            this.numberOfLogins = numberOfLogins;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserLoginCount that = (UserLoginCount) o;

            return !(user != null ? !user.equals(that.user) : that.user != null) && numberOfLogins.equals(that.numberOfLogins);
        }

        @Override
        public int hashCode() {
            int result = user != null ? user.hashCode() : 0;
            result = 31 * result + (numberOfLogins.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return user + ": " + numberOfLogins;
        }
    }
}
