package org.haldokan.edge.interviewquest.bloomberg;

import java.util.*;
import java.util.stream.Collectors;

/**
 * My solution to a Bloomberg interview question
 * <p>
 * you have an vector like this
 * <p>
 * [JFK, LXA, SNA, RKJ, LXA, SNA, NWK, LGR]
 * each 2 group define a route. so,
 * <p>
 * NWK -> LGR
 * JFK -> LXA
 * SNA -> RKJ
 * LXA -> SNA
 * RKJ -> NWK
 * <p>
 * Find the path from departure to destination. note: departure and destination are not known.
 * <p>
 * The final destination should be
 * <p>
 * JFK-> LXA -> SNA -> RKJ -> NWK -> LGR
 * The function signature is something like this
 * <p>
 * vector<string> findPath(vector<string> airports)
 * {
 * <p>
 * }
 * <p>
 * The needed the full path from departure to destination, and you can only pass by one point only once.
 */
public class AirportItinerary {

    public static void main(String[] args) {
        AirportItinerary driver = new AirportItinerary();
        System.out.println(driver.itinerary(Arrays.asList("NWK", "LGR", "JFK", "LXA", "SNA", "RKJ", "LXA", "SNA", "RKJ", "NWK")));
    }

    public List<String> itinerary(List<String> airports) {
        if (airports == null || airports.isEmpty() || airports.size() % 2 != 0) {
            throw new IllegalArgumentException("Invalid input: " + airports);
        }

        Map<String, String> routes = new HashMap<>();

        for (int i = 0; i < airports.size(); i += 2) {
            routes.put(airports.get(i), airports.get(i + 1));
        }
        String airport = getStartAirport(routes);
        List<String> itinerary = new ArrayList<>();

        while (airport != null) {
            itinerary.add(airport);
            airport = routes.get(airport);
        }
        return itinerary;
    }

    private String getStartAirport(Map<String, String> routes) {
        // keyset is just of view to the map so removing changes the map too
        Set<String> startAirports = new HashSet<>(routes.keySet());
        Set<String> endAirports = routes.values().stream().collect(Collectors.toSet());

        startAirports.removeAll(endAirports);
        if (startAirports.size() != 1) {
            throw new RuntimeException("Routes are not configured properly: " + startAirports);
        }

        return startAirports.iterator().next();
    }
}
