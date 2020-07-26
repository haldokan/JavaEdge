package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * My solution to an Amazon interview question - used a hashmap but heavier machinery can also be deployed i.e. DAGs to
 * do topographical sort tho that would be overkill for this problem
 *
 * The Question: 3.5-STAR
 *
 * Write a method that can take in an unordered list of airport pairs visited during a trip, and return the list in order:
 *
 * Unordered: ("ITO", "KOA"), ("ANC", "SEA"), ("LGA", "CDG"), ("KOA", "LGA"), ("PDX", "ITO"), ("SEA", "PDX")
 * Ordered: ("ANC", "SEA"), ("SEA", "PDX"), ("PDX", "ITO"), ("ITO", "KOA"), ("KOA", "LGA"), ("LGA", "CDG")
 */
public class OrderVisitedAirports {

    public static void main(String[] args) {
        List<String[]> airports = orderItinerary(new String[][]{
                new String[]{"ITO", "KOA"},
                new String[]{"ANC", "SEA"},
                new String[]{"LGA", "CDG"},
                new String[]{"KOA", "LGA"},
                new String[]{"PDX", "ITO"},
                new String[]{"SEA", "PDX"},
        });
        airports.forEach(airport -> System.out.printf("%s%n", Arrays.toString(airport)));
    }

    static List<String[]> orderItinerary(String[][] airportPairs) {
        Map<String, String> itinerary = new HashMap<>();
        for (String[] pair : airportPairs) {
            itinerary.put(pair[0], pair[1]);
        }
        String startAirport = Sets.difference(itinerary.keySet(), new HashSet<>(itinerary.values())).stream().findFirst().get();

        String destinationAirport = itinerary.get(startAirport);
        List<String[]> orderedItinerary = new ArrayList<>();

        while (destinationAirport != null) {
            orderedItinerary.add(new String[]{startAirport, destinationAirport });
            startAirport = destinationAirport;
            destinationAirport = itinerary.get(startAirport);
        }
        return orderedItinerary;
    }
}
