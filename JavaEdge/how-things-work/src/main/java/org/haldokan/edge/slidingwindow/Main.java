package org.haldokan.edge.slidingwindow;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static Random rand = new Random();
    private static BlockingQueue<Update> updateQueue = new ArrayBlockingQueue<>(1000);
    private static TrendDetector trendDetector;
    private static String[] tags = new String[] { "world-is-ending", "chicken-run", "women-rule", "Lucy-in-the-sky" };

    public static void main(String[] args) throws Exception {
	trendDetector = new TrendDetector();
	UpdatesSpout spout = new UpdatesSpout(updateQueue, trendDetector, 3);
	spout.start();
	pushUpdates();
    }

    private static void pushUpdates() throws InterruptedException {
	int counter = 0;
	int boundary = 50;
	for (;;) {
	    counter++;
	    Thread.sleep(200);
	    String tag = tags[rand.nextInt(tags.length)];
	    // bias it to women
	    if (rand.nextInt(10) > 6 && counter < boundary)
		tag = tags[2];
	    else if (rand.nextInt(10) > 6 && counter > boundary && counter < 2 * boundary)
		tag = tags[3];

	    updateQueue.put(new Update(tag, rand.nextInt(10), "World is ending in " + rand.nextInt(365) + " days!!!"));
	    System.out.println(counter + ". Trending- " + trendDetector.getTrendingTag());
	    // trendDetector.dumpHeap();
	}
    }
}