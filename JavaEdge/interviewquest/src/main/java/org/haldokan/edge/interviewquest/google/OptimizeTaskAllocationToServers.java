package org.haldokan.edge.interviewquest.google;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - I actually return the allocations, not just the possibility of them
 * the idea is to try to optimize the load on each server at every new task allocation
 * Since the question states that there can be a max of 8 servers and 8 tasks we can implement the
 * optimization by doing an all-sub-sets of a server allocation. This insures that we find the optimal subset of tasks
 * the maximize task allocation to each server.
 * We use a task queue as a staging data structure for tasks. We pop and push tasks from and to the queue
 * depending on which make for better load optimization. The all-sub-set algorithm for n elements is
 * exponential O(2^n). But since the max problem size is fixed (8 in the question) the algorithm becomes O(1) time
 * complexity.
 *
 * The Question (5_STARS):
 * There are at most eight servers in a data center. Each server has got a capacity/memory limit.
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
 * Created by haytham.aldokanji on 6/5/16 .
 */
public class OptimizeTaskAllocationToServers {

    public static void main(String[] args) {
        OptimizeTaskAllocationToServers driver = new OptimizeTaskAllocationToServers();

        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
    }


    public Optional<List<List<Integer>>> allocateTasksToServers(Integer[] serverCapacities, Integer[] taskCapacityReqs) {
        List<List<Integer>> serverAllocations = new ArrayList<>();
        IntStream.range(0, serverCapacities.length).forEach(index -> serverAllocations.add(new ArrayList<>()));

        Deque<Integer> evalDeck = new ArrayDeque<>();
        evalDeck.addAll(Arrays.asList(taskCapacityReqs));

        while (!evalDeck.isEmpty()) {
            Integer taskRequirement = evalDeck.pop();

            boolean allocated = tryAllocatingTask(serverAllocations, serverCapacities, taskRequirement);
            if (!allocated) {
                allocated = tryOptimizingAllocations(evalDeck, serverAllocations, serverCapacities, taskRequirement);
            }
            if (!allocated) {
                System.out.printf("%s - %s%n", "Task requirements: ", Arrays.toString(taskCapacityReqs));
                System.out.printf("%s - %s%n", "Server capacities: ", Arrays.toString(serverCapacities));
                System.out.printf("%s - %s%n", "Server allocations: ", serverAllocations);
                return Optional.empty();
            }
        }
        return Optional.of(serverAllocations);
    }

    private boolean tryAllocatingTask(List<List<Integer>> serverAllocations,
                                      Integer[] serverCapacities, int taskRequirement) {
        int serverIndex = 0;
        for (List<Integer> serverAllocation : serverAllocations) {
            int serverLoad = serverAllocation.stream().collect(Collectors.summingInt(i -> i));
            if (serverLoad + taskRequirement <= serverCapacities[serverIndex++]) {
                serverAllocation.add(taskRequirement);
                return true;
            }
        }
        return false;
    }

    private boolean tryOptimizingAllocations(Deque<Integer> evalDeck, List<List<Integer>> serverAllocations,
                                             Integer[] serverCapacities, int taskRequirement) {
        int serverIndex = 0;
        for (List<Integer> serverAllocation : serverAllocations) {
            List<Integer> replacementIndexes = optimizeAllocation(serverCapacities[serverIndex++],
                    serverAllocation, taskRequirement);

            if (!replacementIndexes.isEmpty()) {
                replacementIndexes.forEach(index -> {
                    evalDeck.add(serverAllocation.get(index));
                    serverAllocation.set(index, -1);
                });

                for (Iterator<Integer> it = serverAllocation.iterator(); it.hasNext(); ) {
                    Integer allocation = it.next();
                    if (allocation == -1) {
                        it.remove();
                    }
                }
                serverAllocation.add(taskRequirement);
                return true;
            }
        }
        return false;
    }

    private List<Integer> optimizeAllocation(int serverCapacity, List<Integer> serverAllocation, int taskRequirement) {
        List<Integer> optimalReplacement = new ArrayList<>();

        if (serverAllocation.stream().collect(Collectors.summingInt(i -> i)) == serverCapacity) {
            return optimalReplacement;
        }

        int dataLen = serverAllocation.size();
        // extra slot to simplify the solution
        int data[] = new int[dataLen + 1];
        computeOptimalReplacement(
                data,
                0,
                dataLen,
                serverCapacity,
                taskRequirement,
                serverAllocation,
                optimalReplacement);

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
        int numCandidates;

        if (currentSolutionLen == dataLen) {
            updateOptimalReplacement(combinations,
                    serverCapacity,
                    taskRequirement,
                    serverAllocation,
                    optimalReplacement);
        } else {
            currentSolutionLen = currentSolutionLen + 1;
            numCandidates = mask(candidates);

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

    private int mask(int[] mask) {
        mask[0] = 1;
        mask[1] = 0;
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

    private void test1() {
        Integer[] taskRequirements = new Integer[]{18, 4, 8, 4, 6, 6, 8, 8, 2};
        Integer[] serverCapacities = new Integer[]{8, 16, 8, 32};
        System.out.printf("Sum of task requirements: %d%n", Arrays.stream(taskRequirements).collect(Collectors.summingInt(i -> i)));
        System.out.printf("Sum of server capacities: %d%n", Arrays.stream(serverCapacities).collect(Collectors.summingInt(i -> i)));

        Optional<List<List<Integer>>> allocations = allocateTasksToServers(serverCapacities, taskRequirements);
        assertThat(allocations.isPresent(), is(true));

        System.out.printf("%s - %s%n", "Task requirements: ", Arrays.toString(taskRequirements));
        System.out.printf("%s - %s%n", "Server capacities: ", Arrays.toString(serverCapacities));
        System.out.printf("%s - %s%n", "Server allocations: ", allocations);
    }

    private void test2() {
        Integer[] taskRequirements = new Integer[]{18, 4, 8, 4, 6, 6, 8, 8, 3};
        Integer[] serverCapacities = new Integer[]{8, 16, 8, 32};
        System.out.printf("Sum of task requirements: %d%n", Arrays.stream(taskRequirements).collect(Collectors.summingInt(i -> i)));
        System.out.printf("Sum of server capacities: %d%n", Arrays.stream(serverCapacities).collect(Collectors.summingInt(i -> i)));

        Optional<List<List<Integer>>> allocations = allocateTasksToServers(serverCapacities, taskRequirements);
        assertThat(allocations.isPresent(), is(false));
    }

    private void test3() {
        Integer[] serverCapacities = new Integer[]{1, 3};
        Integer[] taskRequirements = new Integer[]{4};
        System.out.printf("Sum of task requirements: %d", Arrays.stream(taskRequirements).collect(Collectors.summingInt(i -> i)));
        System.out.printf("Sum of server capacities: %d", Arrays.stream(serverCapacities).collect(Collectors.summingInt(i -> i)));

        Optional<List<List<Integer>>> allocations = allocateTasksToServers(serverCapacities, taskRequirements);
        assertThat(allocations.isPresent(), is(false));
    }

    private void test4() {
        Integer[] taskRequirements = new Integer[]{18, 4, 8, 4, 6, 6, 8, 8, 2, 1, 3, 5, 7, 1};
        Integer[] serverCapacities = new Integer[]{8, 16, 8, 32, 17};
        System.out.printf("Sum of task requirements: %d%n", Arrays.stream(taskRequirements).collect(Collectors.summingInt(i -> i)));
        System.out.printf("Sum of server capacities: %d%n", Arrays.stream(serverCapacities).collect(Collectors.summingInt(i -> i)));

        Optional<List<List<Integer>>> allocations = allocateTasksToServers(serverCapacities, taskRequirements);
        assertThat(allocations.isPresent(), is(true));

        System.out.printf("%s - %s%n", "Task requirements: ", Arrays.toString(taskRequirements));
        System.out.printf("%s - %s%n", "Server capacities: ", Arrays.toString(serverCapacities));
        System.out.printf("%s - %s%n", "Server allocations: ", allocations);
    }

    private void test5() {
        Integer[] taskRequirements = new Integer[]{10, 15, 20};
        Integer[] serverCapacities = new Integer[]{5, 7, 9, 25, 20};
        System.out.printf("Sum of task requirements: %d%n", Arrays.stream(taskRequirements).collect(Collectors.summingInt(i -> i)));
        System.out.printf("Sum of server capacities: %d%n", Arrays.stream(serverCapacities).collect(Collectors.summingInt(i -> i)));

        Optional<List<List<Integer>>> allocations = allocateTasksToServers(serverCapacities, taskRequirements);
        assertThat(allocations.isPresent(), is(true));

        System.out.printf("%s - %s%n", "Task requirements: ", Arrays.toString(taskRequirements));
        System.out.printf("%s - %s%n", "Server capacities: ", Arrays.toString(serverCapacities));
        System.out.printf("%s - %s%n", "Server allocations: ", allocations);
    }

}
