package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - @Inprogress
 *
 * Given the interface below, implement a task scheduler. As a topper support parallel task execution
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
public class TaskScheduler {
    private final Map<String, TaskRunState> taskRunStateMap;
    private final ExecutorService taskRunner;
    private final EventBus taskEventBus;

    private TaskScheduler() {
        taskRunStateMap = new ConcurrentHashMap<>();
        taskRunner = Executors.newCachedThreadPool();
        taskEventBus = new EventBus("TaskEvents");
    }

    public static void main(String[] args) throws Exception {
        TaskScheduler scheduler = TaskScheduler.create();
        Tester tester = scheduler.new Tester();

        tester.testDependencyTopoSort();
        tester.testRunTask();
    }

    public static TaskScheduler create() {
        return new TaskScheduler();
    }

    public Future<Optional<Task[]>> run(Task task) {
        try {
            TaskRunState state = taskRunStateMap.get(task.id);
            if (!(state == null || state == TaskRunState.IN_ERROR)) {
                return CompletableFuture.completedFuture(Optional.empty());
            }
            return taskRunner.submit(new TaskWorker(task));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private enum TaskRunState {PENDING, RUNNING, COMPLETED, IN_ERROR}

    private static class DependencyGraph {
        // edge is not important for our problem so we simply go with a Boolean
        private Table<Task, Task, Boolean> graph = HashBasedTable.create();

        public static DependencyGraph create() {
            return new DependencyGraph();
        }

        public void add(Task task1, Task task2) {
            graph.put(task1, task2, false);
        }

        public Map<Task, Boolean> getAdjacent(Task v) {
            return graph.row(v);
        }

        public boolean contains(Task task1, Task task2) {
            return graph.contains(task1, task2);
        }

        public Task[] sortTopographically(Task task) {
            Map<Task, String> traversalState = new HashMap<>();
            Deque<Task> dependencyDeck = new ArrayDeque<>();
            dfs(task, traversalState, dependencyDeck);
            return Arrays.copyOf(dependencyDeck.toArray(new Task[dependencyDeck.size()]), dependencyDeck.size());
        }

        private void dfs(Task task, Map<Task, String> traversalState, Deque<Task> dependencyDeck) {
            traversalState.put(task, "discovered");
            Set<Task> dependencies = getAdjacent(task).keySet();

            dependencies.stream()
                    .filter(dependency -> traversalState.get(dependency) == null)
                    .forEach(dependency -> dfs(dependency, traversalState, dependencyDeck));

            traversalState.put(task, "processed");
            dependencyDeck.add(task);
        }

        @Override
        public String toString() {
            return "DependencyGraph{" +
                    "graph=" + graph +
                    '}';
        }
    }

    private class TaskWorker implements Callable<Optional<Task[]>> {
        private final Task task;

        public TaskWorker(Task task) {
            this.task = task;
        }

        @Override
        public Optional<Task[]> call() throws Exception {
            DependencyGraph dependencyGraph = DependencyGraph.create();
            Deque<Task> evalQueue = new ArrayDeque<>();
            evalQueue.add(task);

            while (!evalQueue.isEmpty()) {
                Task currentTask = evalQueue.pop();
                for (Task dependency : currentTask.getDependencies()) {
                    evalQueue.add(dependency);
                    dependencyGraph.add(currentTask, dependency);
                }
            }
            Task[] tasks = dependencyGraph.sortTopographically(task);

            List<Task> tasksToRun = new ArrayList<>();
            Arrays.stream(tasks).forEach(t -> {
                TaskRunState newState = taskRunStateMap.compute(t.id, (k, v) ->
                        TaskRunState.COMPLETED != v ? TaskRunState.PENDING : v);
                if (newState == TaskRunState.PENDING) {
                    tasksToRun.add(t);
                }
            });
            Arrays.stream(tasks).forEach(taskRunner::submit);
            return Optional.of(tasksToRun.toArray(new Task[tasksToRun.size()]));
        }
    }

    private class Task implements Runnable {
        private final String id;
        private final LocalTime time;
        private final Set<Task> dependencies;
        private volatile boolean abort;
        private CountDownLatch latch;

        private Task(String id, LocalTime time) {
            this.id = id;
            this.time = time;
            this.dependencies = new HashSet<>();
        }

        public void addDependencies(Task... dependencies) {
            Arrays.stream(dependencies).forEach(this.dependencies::add);
        }

        public void run() {
            if (taskRunStateMap.get(id) != TaskRunState.PENDING) {
                System.out.println("Skipping running task '"
                        + id + "' because it is in state '" + taskRunStateMap.get(id) + "'");
                taskEventBus.post(new RunStateEvent(id, TaskRunState.COMPLETED));
                return;
            }
            latch = new CountDownLatch(dependencies.size());
            for (; ; ) {
                try {
                    latch.await();
                    if (abort) {
                        System.out.println("@" + id + ": abort");
                        taskRunStateMap.put(id, TaskRunState.IN_ERROR);
                        taskEventBus.post(new RunStateEvent(id, TaskRunState.IN_ERROR));
                        return;
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            taskRunStateMap.put(id, TaskRunState.RUNNING);
            System.out.print(id);

            IntStream.range(0, 5).forEach(i -> {
                System.out.print("..." + taskRunStateMap.get(id).toString().toLowerCase());
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    taskRunStateMap.put(id, TaskRunState.IN_ERROR);
                    taskEventBus.post(new RunStateEvent(id, TaskRunState.IN_ERROR));
                    e.printStackTrace();
                }
            });
            taskRunStateMap.put(id, TaskRunState.COMPLETED);
            taskEventBus.post(new RunStateEvent(id, TaskRunState.COMPLETED));
            System.out.println("-->" + taskRunStateMap.get(id).toString().toLowerCase());
        }

        @Subscribe
        public void onEvent(RunStateEvent event) {
            boolean isDependency = dependencies.stream().map(d -> d.id).anyMatch(id -> id.equals(event.taskId));
            if (isDependency) {
                System.out.println("@" + id + ": " + event);
                if (event.state == TaskRunState.IN_ERROR) {
                    abort = true;
                    LongStream.range(0l, latch.getCount()).forEach(i -> latch.countDown());
                } else {
                    latch.countDown();
                }
            }
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
            return id;
        }
    }

    private class RunStateEvent {
        private final String taskId;
        private final TaskRunState state;

        public RunStateEvent(String taskId, TaskRunState state) {
            this.taskId = taskId;
            this.state = state;
        }

        @Override
        public String toString() {
            return taskId + "->" + state;
        }
    }

    private class Tester {
        private Task task1, task2, task3, task4, task5, task6;

        private void makeTasks() {
            task1 = new Task("T1", LocalTime.now());
            task2 = new Task("T2", LocalTime.now().minusHours(1));
            task3 = new Task("T3", LocalTime.now().minusHours(2));
            task4 = new Task("T4", LocalTime.now().minusHours(3));
            task5 = new Task("T5", LocalTime.now().minusHours(4));
            task6 = new Task("T6", LocalTime.now().minusHours(5));

        }

        private void makeDependencies() {
            task1.addDependencies(task2, task3);
            task2.addDependencies(task3, task4);
            task3.addDependencies(task4, task5, task6);
            task4.addDependencies(task5, task6);
            task5.addDependencies(task6);
        }

        private DependencyGraph makeDependencyGraph() {
            makeTasks();
            makeDependencies();
            DependencyGraph dependencyGraph = DependencyGraph.create();

            dependencyGraph.add(task1, task2);
            dependencyGraph.add(task2, task3);
            dependencyGraph.add(task2, task4);
            dependencyGraph.add(task3, task4);
            dependencyGraph.add(task3, task5);
            dependencyGraph.add(task3, task6);
            dependencyGraph.add(task4, task5);
            dependencyGraph.add(task4, task6);
            dependencyGraph.add(task5, task6);

            return dependencyGraph;
        }

        public void testDependencyTopoSort() {
            DependencyGraph dependencyGraph = makeDependencyGraph();
            Task[] dependencies = dependencyGraph.sortTopographically(task1);
            // how tasks 5 and 6 are listed depends on how the dfs traverses the graph
            assertThat(dependencies, is(new Task[]{task6, task5, task4, task3, task2, task1}));
        }

        private void testRunTask() throws Exception {
            makeTasks();
            makeDependencies();

            Task[] expected = new Task[]{task6, task5, task4, task3, task2, task1};

            Task[] tasksRunInOrder = run(task1).get().get();
            assertThat(tasksRunInOrder, is(expected));
            Arrays.stream(expected).forEach(t -> assertThat(taskRunStateMap.get(t.id), is(TaskRunState.COMPLETED)));

            // simulate running a failed task
            taskRunStateMap.put(task1.id, TaskRunState.IN_ERROR);
            tasksRunInOrder = run(task1).get().get();

            assertThat(tasksRunInOrder.length, is(1));
            assertThat(tasksRunInOrder[0], is(task1));
            Arrays.stream(expected).forEach(t -> assertThat(taskRunStateMap.get(t.id), is(TaskRunState.COMPLETED)));

            taskRunner.shutdown();
        }
    }
}
