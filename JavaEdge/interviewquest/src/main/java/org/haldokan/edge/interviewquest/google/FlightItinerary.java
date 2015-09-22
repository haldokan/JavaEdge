package org.haldokan.edge.interviewquest.google;

/**
 * Given an bunch of airline tickets with [from, to], for example [MUC, LHR], [CDG, MUC], [SFO, SJC],
 * [LHR, SFO]. Reconstruct the itinerary in order,
 * for example: [ CDG, MUC, LHR, SFO, SJC ].
 * Created by haytham.aldokanji on 9/22/15.
 */
public class FlightItinerary {
    // This is a graph problem that can be resolved by doing a DFS on the graph and adding the visited nodes to a *queue*.
    // Reading the queue gives the itinerary in order. This is referred to and topographical sort. A DFS with topo sort is
    // implemented in here:
    //org.haldokan.edge.graph.GraphDFS under graph-algorithms
}
