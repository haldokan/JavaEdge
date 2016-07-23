package org.haldokan.edge.interviewquest.amazon;

import java.util.Random;

/**
 * My solution to an Amazon interview question - Random sampling with equal probability from streams of data is done using
 * Knuth's Reservoir algorithm - This post on Stack Overflow gives a good explanation on why the algorithm works:
 * http://stackoverflow.com/questions/12732982/design-a-storage-algorithm/12733515#12733515
 * <p>
 * The Question: 4_STAR
 * <p>
 * You have a stream of sentences. you don't know total number of sentences until you exhaust the stream. you have to
 * choose one sentence randomly with equal probability from the input stream.
 * you don't have space to store all sentences at your end.
 * <p>
 * Created by haytham.aldokanji on 7/22/16.
 */
public class SelectingRandomItemFromInfiniteStream {
    private final String[] reservoir;
    private int runningSentenceCount;
    private Random random = new Random(System.currentTimeMillis());

    public SelectingRandomItemFromInfiniteStream(int reservoirSize) {
        reservoir = new String[reservoirSize];
    }

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws InterruptedException {
        int reservoirSize = 100;
        SelectingRandomItemFromInfiniteStream randomSelector = new SelectingRandomItemFromInfiniteStream(reservoirSize);

        new Thread(() -> {
            for (int i = 0; i < reservoirSize * 10; i++) {
                randomSelector.probablyAddToReservoir("sentence" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Thread.sleep(1000);

        for (int i = 0; i < 1000; i++) {
            System.out.printf("%s,", randomSelector.getRandomSentence());
            if (i > 0 && i % 10 == 0) {
                System.out.printf("%n%d%n", randomSelector.runningSentenceCount);
            }
            Thread.sleep(100);
        }
    }

    public void probablyAddToReservoir(String sentence) {
        if (runningSentenceCount < reservoir.length) {
            reservoir[runningSentenceCount] = sentence;
        } else {
            int randomIndex = random.nextInt(runningSentenceCount);
            if (randomIndex < reservoir.length) {
                reservoir[randomIndex] = sentence;
            }
        }
        runningSentenceCount++;
    }

    public String getRandomSentence() {
        int sampleSize = Math.min(runningSentenceCount, reservoir.length);
        if (sampleSize == 0) {
            return null;
        }
        return reservoir[random.nextInt(sampleSize)];
    }
}
