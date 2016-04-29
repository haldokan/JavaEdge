package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - I think I mis-read the question so I did not use the know(a, b) function
 * TODO: provide another impl that uses the know(a, b) func.
 *
 * Given a function KNOWS(A,B), which returns 1 if A knows B and 0 if A does not know B.
 * <p>
 * A Celebrity is one who does not know anyone,
 * and one who is known by everybody.
 * <p>
 * For a list of N people, find all celebrities in linear time.
 * <p>
 * Created by haytham.aldokanji on 4/28/16.
 */
public class IdentifyingCelebritiesInSocialNetwork {

    public static void main(String[] args) {
        IdentifyingCelebritiesInSocialNetwork driver = new IdentifyingCelebritiesInSocialNetwork();

        List<String> connections = Lists.newArrayList(
                "A,B", "A,D", "B,A", "D,B",
                "A,C1", "A,C2", "B,C1", "B,C2",
                "D,C1", "D,C2"
        );

        Set<String> celebrities = driver.celebrities(connections);
        System.out.println(celebrities);
        assertThat(celebrities, is(Sets.newHashSet("C1", "C2")));

        connections = Lists.newArrayList(
                "A,B", "A,D", "B,A", "D,B",
                "A,C1", "A,C2", "B,C1", "B,C2",
                "D,C1", "D,C2", "A,C3", "B,C3",
                "D,C3", "C3,C2"
        );
        celebrities = driver.celebrities(connections);
        System.out.println(driver.celebrities(connections));
        assertThat(celebrities, is(Sets.newHashSet("C2")));

        connections = Lists.newArrayList(
                "A,B", "A,D", "B,A", "D,B",
                "A,C1", "A,C2", "B,C1", "B,C2",
                "D,C1", "D,C2", "A,C3", "B,C3",
                "D,C3", "C1,C3", "C2,C3", "C3,A"
        );
        celebrities = driver.celebrities(connections);
        System.out.println(driver.celebrities(connections));
        assertThat(celebrities, is(Sets.newHashSet()));

        connections = Lists.newArrayList("A,B");
        celebrities = driver.celebrities(connections);
        System.out.println(driver.celebrities(connections));
        assertThat(celebrities, is(Sets.newHashSet("B")));
    }

    //Let's assume that we a list of strings which each string has this format A,B (A knows B)
    public Set<String> celebrities(List<String> connections) {
        Multimap<String, String> network = HashMultimap.create();
        connections.stream().map(s -> s.split(",")).forEach(pair -> network.put(pair[0], pair[1]));

        Set<String> peopleKnownByAll = network.asMap()
                .values()
                .stream()
                .map(Sets::newHashSet)
                .reduce((set1, set2) -> {
                    HashSet<String> intersection = new HashSet<>();
                    Sets.intersection(set1, set2).copyInto(intersection);
                    return intersection;
                }).get();

        Set<String> peopleWhoKnowOthers = network.keySet();
        return Sets.difference(peopleKnownByAll, peopleWhoKnowOthers);
    }
}
