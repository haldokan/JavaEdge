package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.*;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - I provide 2 solutions: one using standard Java api and another using Guava
 * The Question: 4_STAR
 * <p>
 * Given N scientists and K black holes, each scientist can query on radius, size and temperature of a black hole,
 * what data structure would you use? The following queries are important:
 * <p>
 * Which scientist had queried on which black hole.
 * What were the queries made by that scientist
 * <p>
 * Created by haytham.aldokanji on 7/28/16.
 */
public class ScientistsAndBlackholes {
    // caches that store scientists and blackholes info
    private final Map<String, Scientist> scientists = new HashMap<>();
    private final Map<String, Blackhole> blackholes = new HashMap<>();
    // java-api
    private final Map<String, Set<String>> scientestsByBlackhole = new HashMap<>();
    private final Map<String, Map<String, Set<QueryType>>> queriesByScientistAndBlackhole = new HashMap<>();
    // guave-api
    private final SetMultimap<String, String> scientestsByBlackhole_quava = HashMultimap.create();
    private final Table<String, String, Set<QueryType>> queriesByScientistAndBlackhole_quava = HashBasedTable.create();

    public static void main(String[] args) {
        ScientistsAndBlackholes driver = new ScientistsAndBlackholes();
        driver.test();
    }

    public Set<Scientist> getScientistsWhoQueriedBlackhole(String blackhole) {
        Set<String> scientistIds = scientestsByBlackhole.getOrDefault(blackhole, new HashSet<>());

        ImmutableSet.Builder<Scientist> result = ImmutableSet.builder();
        scientistIds.forEach(id -> result.add(scientists.get(id)));
        return result.build();
    }

    public Set<Scientist> scientistsWhoQueriedBlackhole_quava(String blackhole) {
        Set<String> scientistIds = scientestsByBlackhole_quava.get(blackhole);

        ImmutableSet.Builder<Scientist> result = ImmutableSet.builder();
        scientistIds.forEach(id -> result.add(scientists.get(id)));
        return result.build();
    }

    public Set<QueryType> queriesPerformedByScientistOnBlackhole(String scientist, String blackhole) {
        Set<QueryType> queries = queriesByScientistAndBlackhole.getOrDefault(scientist, new HashMap<>())
                .getOrDefault(blackhole, new HashSet<>());
        return ImmutableSet.copyOf(queries);
    }

    public Set<QueryType> queriesPerformedByScientistOnBlackhole_quava(String scientist, String blackhole) {
        Set<QueryType> queries = queriesByScientistAndBlackhole_quava.get(scientist, blackhole);
        return ImmutableSet.copyOf(queries == null ? new HashSet<>() : queries);
    }

    private void test() {
        Random random = new Random();

        IntStream.range(0, 10).forEach(index -> {
            String username = "username" + index;
            scientists.put(username, new Scientist(username));
            scientists.put(username + index, new Scientist(username + index));

            String blackholeId = "blackhole" + index;
            blackholes.put(blackholeId, new Blackhole(blackholeId, random.nextDouble(), random.nextDouble(), random.nextInt()));
        });

        String[] allScientists = scientists.keySet().toArray(new String[scientists.size()]);
        String[] allBlackholes = blackholes.keySet().toArray(new String[blackholes.size()]);

        IntStream.range(0, 1000).forEach(index -> {
            String scientist = allScientists[random.nextInt(allScientists.length)];
            String blackhole = allBlackholes[random.nextInt(allBlackholes.length)];
            QueryType queryType = QueryType.values()[random.nextInt(3)];

            // java-api
            Set<String> blackholeScientists = scientestsByBlackhole.computeIfAbsent(blackhole, v -> new HashSet<>());
            blackholeScientists.add(scientist);

            // guava-api
            scientestsByBlackhole_quava.put(blackhole, scientist);

            // java-api
            Map<String, Set<QueryType>> queriesByScientist = queriesByScientistAndBlackhole.computeIfAbsent(scientist, v -> new HashMap<>());
            Set<QueryType> queryTypes = queriesByScientist.computeIfAbsent(blackhole, v -> new HashSet<>());
            queryTypes.add(queryType);

            //guava-api
            if (!queriesByScientistAndBlackhole_quava.contains(scientist, blackhole)) {
                queriesByScientistAndBlackhole_quava.put(scientist, blackhole, new HashSet<>());
            }
            queriesByScientistAndBlackhole_quava.get(scientist, blackhole).add(queryType);
        });

        for (String blackhole : allBlackholes) {
            assertThat(getScientistsWhoQueriedBlackhole(blackhole)
                    .equals(scientistsWhoQueriedBlackhole_quava(blackhole)), is(true));
        }

        for (String scientist : allScientists) {
            for (String blackhole : allBlackholes) {
                assertThat(queriesPerformedByScientistOnBlackhole(scientist, blackhole)
                        .equals(queriesPerformedByScientistOnBlackhole_quava(scientist, blackhole)), is(true));
            }
        }
    }

    private enum QueryType {RADIUS, SIZE, TEMPERATURE}

    private static class Scientist {
        private final String username;

        public Scientist(String username) {
            this.username = username;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Scientist scientist = (Scientist) o;

            return username.equals(scientist.username);

        }

        @Override
        public int hashCode() {
            return username.hashCode();
        }

        @Override
        public String toString() {
            return "Scientist{" +
                    "username='" + username + '\'' +
                    '}';
        }
    }

    private static final class Blackhole {
        private final String id;
        private final double radius, size;
        private final int temperature;

        public Blackhole(String id, double radius, double size, int temperature) {
            this.id = id;
            this.radius = radius;
            this.size = size;
            this.temperature = temperature;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Blackhole blackhole = (Blackhole) o;
            return id.equals(blackhole.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return "Blackhole{" +
                    "id='" + id + '\'' +
                    ", radius=" + radius +
                    ", size=" + size +
                    ", temperature=" + temperature +
                    '}';
        }
    }
}
