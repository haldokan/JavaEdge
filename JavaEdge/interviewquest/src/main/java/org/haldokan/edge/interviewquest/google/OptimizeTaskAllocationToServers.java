package org.haldokan.edge.interviewquest.google;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Inprogress There are at most eight servers in a data center. Each server has got a capacity/memory limit.
 * There can be at most 8 tasks that need to be scheduled on those servers. Each task requires certain capacity/memory
 * to run, and each server can handle multiple tasks as long as the capacity limit is not hit.
 * <p>
 * Write a program to see if all of the given tasks can be scheduled or not on the servers.
 * <p>
 * Ex:
 * Servers capacity limits: 8, 16, 8, 32
 * Tasks capacity needs: 18, 4, 8, 4, 6, 6, 8, 8
 * For this example, the program should say 'true'.
 * <p>
 * Ex2:
 * Server capacity limits: 1, 3
 * Task capacity needs: 4
 * For this example, program should return false.
 * <p>
 * Created by haytham.aldokanji on 6/5/16.
 */
public class OptimizeTaskAllocationToServers {

    public static void main(String[] args) {
    }

    public Optional<List<List<Integer>>> allocateTasksToServers(int[] serverCapacities, Integer[] taskCapacityReqs) {
        List<List<Integer>> serverAllocations = new ArrayList<>();
        Deque<Integer> evalDeck = new ArrayDeque<>();
        evalDeck.addAll(Arrays.asList(taskCapacityReqs));

        while (!evalDeck.isEmpty()) {
            Integer taskRequirement = evalDeck.pop();
            int serverIndex = 0;
            boolean allocated = false;

            for (List<Integer> serverAllocation : serverAllocations) {
                int serverLoad = serverAllocation.stream().collect(Collectors.summingInt(i -> i));
                if (serverLoad + taskRequirement < serverCapacities[serverIndex]) {
                    serverAllocation.add(taskRequirement);
                    allocated = true;
                }
                serverIndex++;
            }

            if (!allocated) {
                serverIndex = 0;
                for (List<Integer> serverAllocation : serverAllocations) {
                    List<Integer> replacementIndexes = optimizeAllocation(serverCapacities[serverIndex++],
                            serverAllocation, taskRequirement);

                    if (!replacementIndexes.isEmpty()) {
                        replacementIndexes.forEach(index -> {
                            evalDeck.add(serverAllocation.get(index));
                            serverAllocation.remove(index);
                            serverAllocation.add(taskRequirement);
                        });
                        serverAllocation.add(taskRequirement);

                        allocated = true;
                        break;
                    }
                    serverIndex++;
                }
            }
            if (!allocated) {
                return Optional.empty();
            }
        }
        return Optional.of(serverAllocations);
    }

    private List<Integer> optimizeAllocation(int serverCapacity, List<Integer> serverAllocation, int taskRequirement) {
        List<Integer> optimalReplacement = new ArrayList<>();

        if (serverAllocation.stream().collect(Collectors.summingInt(i -> i)) == serverCapacity) {
            return optimalReplacement;
        }

        int dataLen = serverAllocation.size();
        // extra slot to simplify the solution
        int data[] = new int[dataLen + 1];
        computeOptimalReplacement(data, 0, dataLen, serverCapacity, taskRequirement, serverAllocation, optimalReplacement);
        return optimalReplacement;
    }

    public void computeOptimalReplacement(int[] combinations,
                                          int currentSolutionLen,
                                          int dataLen,
                                          int serverCapacity,
                                          int taskRequirement,
                                          List<Integer> serverAllocation,
                                          List<Integer> optimalReplacement) {
        int[] candidates = new int[2];
        int numCandidates = 0;

        if (currentSolutionLen == dataLen) {
            updateOptimalReplacement(combinations,
                    serverCapacity,
                    taskRequirement,
                    serverAllocation,
                    optimalReplacement);
        } else {
            currentSolutionLen = currentSolutionLen + 1;
            numCandidates = candidates(candidates);
            for (int i = 0; i < numCandidates; i++) {
                combinations[currentSolutionLen] = candidates[i];
                computeOptimalReplacement(combinations,
                        currentSolutionLen,
                        dataLen,
                        serverCapacity,
                        taskRequirement,
                        serverAllocation,
                        optimalReplacement);
            }
        }
    }

    private int candidates(int[] candidates) {
        candidates[0] = 1;
        candidates[1] = 0;
        return 2;
    }

    private void updateOptimalReplacement(int[] combinations,
                                          int serverCapacity,
                                          int taskRequirement,
                                          List<Integer> serverAllocation,
                                          List<Integer> optimalReplacement) {
        List<Integer> currentReplacementIndices = new ArrayList<>();
        for (int i = 1; i <= serverAllocation.size(); i++) {
            if (combinations[i] == 1) {
                currentReplacementIndices.add(i - 1);
            }
        }
        List<Integer> currentReplacementValues = currentReplacementIndices.stream()
                .map(serverAllocation::get)
                .collect(Collectors.toList());

        int currentOptimalReplacementSum = optimalReplacement.stream()
                .map(serverAllocation::get)
                .collect(Collectors.summingInt(i -> i));

        int currentReplacementSum = currentReplacementValues.stream().collect(Collectors.summingInt(i -> i));
        int serverLoad = serverAllocation.stream().collect(Collectors.summingInt(i -> i));


        if (currentReplacementSum < taskRequirement
                && currentReplacementSum > currentOptimalReplacementSum
                && (taskRequirement - currentReplacementSum) + serverLoad <= serverCapacity) {
            optimalReplacement.clear();
            optimalReplacement.addAll(currentReplacementIndices);
        }
    }
}
