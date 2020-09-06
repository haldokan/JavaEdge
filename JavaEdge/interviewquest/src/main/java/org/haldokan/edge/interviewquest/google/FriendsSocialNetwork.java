package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import java.util.Collection;

/**
 * My solution to a Google interview question - hard to know what the interviewer wanted. I maintain a guava Table
 * that keeps track of the number of shared friends b/w every 2 persons.
 *
 * The Question: 4-STAR
 *
 * You are in charge of designing a small in-memory social network with the basic functionality of adding friendship
 * between two people via an AddFriendship function, and a GetSuggestedFriends function for a particular user in the
 * network. The criteria is to pick someone with whom the given user has the most number of friends in common.
 *
 * Start by discussing the most suitable data structure, and implement all the objects and functions.
 * 09/06/20
 */
public class FriendsSocialNetwork {
    private final Multimap<String, String> friends = LinkedHashMultimap.create();
    private final Table<String, String, Integer> sharedFriendsCount = HashBasedTable.create();

    public static void main(String[] args) {
        FriendsSocialNetwork driver = new FriendsSocialNetwork();
        driver.test1();
        driver.test2();
    }

    public void addFriend(String person, String friend) {
        // todo guard against adding same friendship twice
        friends.put(person, friend);
        friends.put(friend, person);

        Integer count = sharedFriendsCount.get(person, friend);
        int updateCount = count == null ? 1 : count + 1;

        sharedFriendsCount.put(person, friend, updateCount);
        sharedFriendsCount.put(friend, person, updateCount);

        for (String keyedPerson : friends.keySet()) {
            if (!(keyedPerson.equals(person) || keyedPerson.equals(friend))) {
                Collection<String> friendSet = friends.get(keyedPerson);
                if (friendSet.contains(friend)) {
                    Integer count1 = sharedFriendsCount.get(person, keyedPerson);
                    int updateCount1 = count1 == null ? 1 : count1 + 1;
                    sharedFriendsCount.put(person, keyedPerson, updateCount1);
                    sharedFriendsCount.put(keyedPerson, person, updateCount1);
                }
            }
        }
    }

    public String suggestFriend(String person) {
        int maxCount = 0;
        String suggestedFriend = null;
        for (String currentPerson  : friends.keySet()) {
            // we don't want them to be friends already
            if (!currentPerson.equals(person) && !friends.get(person).contains(currentPerson)) {
                Integer count = sharedFriendsCount.get(currentPerson, person);
                if (count == null) {
                    count = 0;
                }
                if (count > maxCount) {
                    maxCount = count;
                    suggestedFriend = currentPerson;
                }
            }
        }
        return suggestedFriend;
    }

    private void test1() {
        addFriend("A", "A1");
        addFriend("B", "B1");
        addFriend("C", "C1");
        addFriend("D", "D1");

        addFriend("A", "B1");
        addFriend("A", "D1");
        addFriend("C", "B1");
        addFriend("C", "D1");

        System.out.printf("suggested friend for A: %s%n", suggestFriend("A"));
        System.out.printf("suggested friend for C: %s%n", suggestFriend("C"));
    }

    private void test2() {
        addFriend("A", "A1");
        addFriend("B", "B1");
        addFriend("C", "C1");
        addFriend("D", "D1");
        addFriend("E", "E1");
        addFriend("F", "F1");

        addFriend("A", "B1");
        addFriend("C", "F1");
        addFriend("A", "F1");
        addFriend("A", "E1");
        addFriend("C", "E1");

        System.out.printf("suggested friend for A: %s%n", suggestFriend("A"));
    }
}
