package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @Inprogress Given the interface below, implement a task scheduler. As a topper support parallel task execution
 * <p>
 * interface Task {
 * void Run();
 * Set<Task> GetDependencies();
 * }
 * Additionally, the task scheduler should follow two rules.
 * <p>
 * 1. Each task may only be executed once.
 * 2. The dependencies of a task should be executed before the task itself.
 * <p>
 * Created by haytham.aldokanji on 5/24/16.
 */
public class JobScheduler {

    private static class DependencyGraphTopoSorter {
        private Map<Task, State> traversalState = new HashMap<>();
        private Deque<Task> dependencyDeck = new ArrayDeque<>();
        private Task[] topoSortedDependencies;
        private boolean used;

        public static DependencyGraphTopoSorter create() {
            return new DependencyGraphTopoSorter();
        }

        public void topoSortDependencies(TaskDependencyGraph dependencyGraph, Task task) {
            if (used) {
                throw new IllegalStateException("Must call topographic sort on a new instance");
            }
            used = true;
            dfs(dependencyGraph, task);
            topoSortedDependencies = dependencyDeck.toArray(new Task[dependencyDeck.size()]);
        }

        private void dfs(TaskDependencyGraph dependencyGraph, Task task) {
            traversalState.put(task, State.DISCOVERED);
            Set<Task> dependencies = dependencyGraph.getAdjacent(task).keySet();

            dependencies.stream()
                    .filter(dependency -> traversalState.get(dependency) == null)
                    .forEach(dependency -> dfs(dependencyGraph, dependency));

            traversalState.put(task, State.PROCESSED);
            dependencyDeck.add(task);
        }

        public Task[] getDependencies(Task task) {
            int taskIndex = Arrays.binarySearch(topoSortedDependencies, task);
            return Arrays.copyOfRange(topoSortedDependencies, taskIndex + 1, topoSortedDependencies.length);
        }

        public Task[] getDependencies() {
            return Arrays.copyOf(topoSortedDependencies, topoSortedDependencies.length);
        }

        private enum State {DISCOVERED, PROCESSED}
    }

    private static class Task {
        private final String id;
        private final LocalTime time;
        private final Set<Task> dependencies;

        private Task(String id, LocalTime time, Set<Task> dependencies) {
            this.id = id;
            this.time = time;
            this.dependencies = new HashSet<>(dependencies);
        }

        public static Task create(String id, LocalTime time, Set<Task> dependencies) {
            return new Task(id, time, dependencies);
        }

        public void run() {
            IntStream.range(0, 3).forEach(i -> {
                System.out.println(id);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        public Set<Task> getDependencies() {
            return dependencies;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Task task = (Task) o;

            return !(id != null ? !id.equals(task.id) : task.id != null);

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "id='" + id + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    private static class TaskDependencyGraph {
        // edge is not important for our problem so we simply go with a Boolean
        private Table<Task, Task, Boolean> graph = HashBasedTable.create();

        public static TaskDependencyGraph create() {
            return new TaskDependencyGraph();
        }

        public void add(Task task1, Task task2) {
            graph.put(task1, task2, false);
        }

        public Set<Task> getTasks() {
            return graph.rowKeySet();
        }

        public Map<Task, Boolean> getAdjacent(Task v) {
            return graph.row(v);
        }

        public boolean contains(Task task1, Task task2) {
            return graph.contains(task1, task2);
        }

        public void clear() {
            graph.clear();
        }

        @Override
        public String toString() {
            return "TaskDependencyGraph{" +
                    "graph=" + graph +
                    '}';
        }
    }
}
