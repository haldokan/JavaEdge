package org.haldokan.edge.interviewquest.google;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * My solution to a Google interview question - I supported concurrency on two levels: the job level, in that the tasks
 * in one job submission run concurrently with other job submissions. The 2nd level is within the same job: in the same
 * job submission tasks that are not dependent on each other run concurrently. Dependent tasks run only after the successful
 * completion of their dependencies: I achieved that with a DAG DFS that sorted the tasks topographically. When a task fails
 * all its dependent tasks are not run and are marked as failed. Re-submission of a failed job runs only the failed tasks.
 * Tasks that complete successfully cannot be rerun because the task scheduler keeps a record of task run statuses. Task
 * run statuses are communicated to other running tasks via an event bus (Guava EventBus). Every task runs in its own
 * pooled thread and it is 'awaited' on a latch that is counted down every time a dependency of the task completes successfully.
 * No synchronization constructs were used in this solution apart from the R/W lock built into to the Java ConcurrentHashMap.
 *
 * The Question (5_STARS):
 *
 * Given the interface below, implement a task scheduler that concurrently runs tasks
 * interface Task {
 * void Run();
 * Set<Task> GetDependencies();
 * }
 * Additionally, the task scheduler should follow two rules:
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
        tester.testGoodRun();
        tester.testTaskFailurePropagation();
        tester.testFailedTaskRerun();

        scheduler.taskRunner.shutdown();
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
        // edge is not important for our solution so we simply go with a Boolean
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
            tasksToRun.forEach(Task::registerWithEventBus);
            tasksToRun.forEach(taskRunner::submit);
            return Optional.of(tasksToRun.toArray(new Task[tasksToRun.size()]));
        }
    }

    private class Task implements Runnable {
        private final String id;
        private final LocalTime time;
        private final Set<Task> dependencies;
        private volatile boolean aborted;
        private CountDownLatch latch;

        private Task(String id, LocalTime time, Task... dependencies) {
            this.id = id;
            this.time = time;
            this.dependencies = Arrays.stream(dependencies).collect(Collectors.toSet());
        }

        public void registerWithEventBus() {
            latch = new CountDownLatch((int) this.dependencies.stream()
                    .filter(d -> TaskRunState.PENDING == taskRunStateMap.get(d.id))
                    .count());
            taskEventBus.register(this);
        }

        public void run() {
            for (; ; ) {
                try {
                    latch.await();
                    if (aborted) {
                        System.out.printf("@%s->aborted%n", id);
                        updateState(TaskRunState.IN_ERROR);
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //once started to run or aborted we are not interested in any event notifications
            taskEventBus.unregister(this);
            if (aborted) {
                return;
            }
            updateState(TaskRunState.RUNNING);
            System.out.printf("@%s", id);
            for (int i = 0; i < 5; i++) {
                System.out.printf("...%s", taskRunStateMap.get(id));
                try {
                    Thread.sleep(100);
                    if (time == null) {
                        throw new RuntimeException("Simulated failure for task: " + id);
                    }
                } catch (Exception e) {
                    updateState(TaskRunState.IN_ERROR);
                    System.out.printf("%s%n", e);
                    // this exception shows only when the Future.get is called
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("%n");
            updateState(TaskRunState.COMPLETED);
        }

        @Subscribe
        public void onEvent(RunStateEvent event) {
            // not interested in the RUNNING state including for this task dependencies
            if (event.state == TaskRunState.RUNNING) {
                return;
            }
            boolean isDependency = dependencies.stream().map(d -> d.id).anyMatch(id -> id.equals(event.taskId));
            if (isDependency) {
                System.out.printf("@%s-sub->%s%n", id, event);
                if (event.state == TaskRunState.IN_ERROR) {
                    aborted = true;
                    LongStream.range(0l, latch.getCount()).forEach(i -> latch.countDown());
                } else if (event.state == TaskRunState.COMPLETED) {
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

        private void updateState(TaskRunState state) {
            System.out.printf("@%s-pub->%s%n", id, state);
            taskRunStateMap.put(id, state);
            taskEventBus.post(new RunStateEvent(id, state));
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
            return taskId + ":" + state;
        }
    }

    private class Tester {
        private Task task1, task2, task3, task4, task5, task6;

        private void makeTasks(boolean addFailingTask) {
            //mind the order!
            task6 = new Task("T6", LocalTime.now().minusHours(5));
            task5 = new Task("T5", LocalTime.now().minusHours(4), task6);
            LocalTime task4Time = addFailingTask ? null : LocalTime.now().minusHours(3);
            task4 = new Task("T4", task4Time, task5, task6);
            task3 = new Task("T3", LocalTime.now().minusHours(2), task4, task5, task6);
            task2 = new Task("T2", LocalTime.now().minusHours(1), task3, task4);
            task1 = new Task("T1", LocalTime.now(), task2);
        }

        private DependencyGraph makeDependencyGraph() {
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

        private void pollAndAssertTaskRunState(Task[] tasks, TaskRunState[] states) throws InterruptedException {
            Stopwatch watch = Stopwatch.createStarted();
            for (; ; ) {
                Thread.sleep(50);
                boolean done = true;
                for (int i = 0; i < tasks.length; i++) {
                    done &= states[i] == taskRunStateMap.get(tasks[i].id);
                }
                if (done) {
                    break;
                }
                if (watch.elapsed(TimeUnit.SECONDS) > 5) {
                    fail("timed out waiting: "
                            + Arrays.toString(tasks) + "\n"
                            + Arrays.toString(states) + "\n"
                            + taskRunStateMap);
                }
            }
        }

        private void testDependencyTopoSort() {
            makeTasks(false);
            DependencyGraph dependencyGraph = makeDependencyGraph();
            Task[] dependencies = dependencyGraph.sortTopographically(task1);
            assertThat(dependencies, is(new Task[]{task6, task5, task4, task3, task2, task1}));
        }

        private void testGoodRun() throws Exception {
            taskRunStateMap.clear();
            makeTasks(false);

            Task[] expected = new Task[]{task6, task5, task4, task3, task2, task1};
            TaskRunState[] states = new TaskRunState[]{
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED
            };
            Task[] tasksRunInOrder = run(task1).get().get();
            pollAndAssertTaskRunState(expected, states);
            assertThat(tasksRunInOrder, is(expected));
        }

        private void testTaskFailurePropagation() throws Exception {
            taskRunStateMap.clear();
            makeTasks(true);

            Task[] expected = new Task[]{task6, task5, task4, task3, task2, task1};
            Task[] tasksRunInOrder = run(task1).get().get();
            // task4 failed which means that all tasks dependent on it will be marked as failed. However it own dependencies
            // will be marked as successful.
            TaskRunState[] states = new TaskRunState[]{
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.IN_ERROR, TaskRunState.IN_ERROR,
                    TaskRunState.IN_ERROR, TaskRunState.IN_ERROR
            };
            pollAndAssertTaskRunState(expected, states);
            assertThat(tasksRunInOrder, is(expected));
        }

        private void testFailedTaskRerun() throws Exception {
            taskRunStateMap.clear();
            makeTasks(true);

            Task[] expected = new Task[]{task6, task5, task4, task3, task2, task1};
            Task[] tasksRunInOrder = run(task1).get().get();
            // task4 failed which means that all tasks dependent on it will be marked as failed. However it own dependencies
            // will be marked as successful.
            TaskRunState[] states = new TaskRunState[]{
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.IN_ERROR, TaskRunState.IN_ERROR,
                    TaskRunState.IN_ERROR, TaskRunState.IN_ERROR
            };
            pollAndAssertTaskRunState(expected, states);
            assertThat(tasksRunInOrder, is(expected));

            // we failed task4 so all its dependent tasks also failed. Now let's rerun the job and see that only the failed
            // tasks run
            makeTasks(false);
            //only the failed tasks were run
            Task[] expected2 = new Task[]{task4, task3, task2, task1};

            tasksRunInOrder = run(task1).get().get();
            // all tasks are now completed
            states = new TaskRunState[]{
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED,
                    TaskRunState.COMPLETED, TaskRunState.COMPLETED
            };
            pollAndAssertTaskRunState(expected, states);
            assertThat(tasksRunInOrder, is(expected2));
        }
    }
}
