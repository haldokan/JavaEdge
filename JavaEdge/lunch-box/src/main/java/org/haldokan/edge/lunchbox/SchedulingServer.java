package org.haldokan.edge.lunchbox;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * My solution to a Linkedin interview Question. In a fuller solution we have to provide shutdown, etc.
 * 
 * A server receives requests from different clients...each client send a Runnable job and time on which this job should
 * be run. Write a java program that would accept these jobs and run each job at the required time. Processing the jobs
 * should be multi-threaded
 * 
 * @author haldokan
 *
 */
public class SchedulingServer {
    private BlockingDeque<Job> serviceQu = new LinkedBlockingDeque<>();
    private BlockingDeque<String> signalQu = new LinkedBlockingDeque<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Queue<Job> schedule = new PriorityQueue<>(new Comparator<Job>() {
	@Override
	public int compare(Job o1, Job o2) {
	    return o1.runTime.compareTo(o2.runTime);
	}
    });

    public static void main(String[] args) throws Exception {
	SchedulingServer server = new SchedulingServer();
	Thread thrd = new Thread(new Runnable() {
	    @Override
	    public void run() {
		server.startup();
	    }
	});
	thrd.start();
	Thread.sleep(1000);

	LocalTime t1 = LocalTime.now().plusSeconds(5);
	Runnable task1 = new Runnable() {
	    @Override
	    public void run() {
		System.out.println("Ran Task1 " + t1 + "/" + LocalTime.now());
	    }
	};
	server.scheduleJob(task1, t1);

	LocalTime t2 = LocalTime.now();
	Runnable task2 = new Runnable() {
	    @Override
	    public void run() {
		System.out.println("Ran Task2 " + t2 + "/" + LocalTime.now());
	    }
	};
	server.scheduleJob(task2, t2);
    }

    public void startup() {
	executor.submit(new Runnable() {
	    @Override
	    public void run() {
		runJobProcessor();
	    }
	});
	executor.submit(new Runnable() {
	    @Override
	    public void run() {
		runJobScheduler();
	    }
	});
	runForever();
    }

    public void scheduleJob(Runnable r, LocalTime time) {
	serviceQu.offer(new Job(r, time, signalQu).startTimer());
    }

    private void runForever() {
	CountDownLatch latch = new CountDownLatch(1);
	try {
	    latch.await();
	} catch (InterruptedException e) {
	}
    }

    private void runJobProcessor() {
	for (;;) {
	    try {
		Job job = serviceQu.take();
		if (job.due) {
		    executor.submit(job);
		} else {
		    addToSchedule(job);
		}
	    } catch (InterruptedException e) {
	    }
	}
    }

    private void runJobScheduler() {
	for (;;) {
	    try {
		signalQu.take();
		executor.submit(getJobFromSchedule());
	    } catch (InterruptedException e) {
	    }
	}
    }

    // PQ is not thread-safe so have to synch
    private synchronized void addToSchedule(Job job) {
	schedule.add(job);
    }

    private synchronized Job getJobFromSchedule() {
	return schedule.poll();
    }

    private static class Job extends Thread {
	private final LocalTime runTime;
	private final BlockingQueue<String> signalQu;
	private volatile boolean due;

	public Job(Runnable r, LocalTime time, BlockingQueue<String> signalQu) {
	    super(r);
	    this.runTime = time;
	    this.signalQu = signalQu;
	}

	public Job startTimer() {
	    ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
	    es.schedule(new Runnable() {
		@Override
		public void run() {
		    due = true;
		    signalQu.offer("take him!");
		}
	    }, LocalTime.now().until(runTime, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
	    es.shutdown();
	    return this;
	}

	@Override
	public void start() {
	    super.start();
	}
    }
}
