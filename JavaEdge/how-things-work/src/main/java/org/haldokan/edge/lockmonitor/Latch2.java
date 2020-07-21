package org.haldokan.edge.lockmonitor;

/**
 * Latch implementation using wait and notify
 * The Question: 3_STAR
 *
 * @author haldokan
 */
public class Latch2 {
    private static final Object LOCK = new Object();
    private volatile int count;

    public Latch2(int count) {
        this.count = count;
    }

    public static void main(String[] args) throws InterruptedException {
        int latchCount = 3;
        Latch2 latch2 = new Latch2(latchCount);

        Thread thread1 = new Thread(new TestThread(latch2));
        Thread thread2 = new Thread(new TestThread(latch2));

        thread1.start();
        thread2.start();

        Thread.sleep(3000);
        // we can count down as low as we want
        for (int i = 0; i < latchCount; i++) {
            latch2.countdown(); // not how the count down is done by a thread that's not locked on the latch
            System.out.println("counting down..." + latch2.count());
        }
    }

    public void countdown() {
        if (count == 0) {
            return;
        }
        synchronized (LOCK) {
            count--;
            if (count == 0) {
                // all threads run at this point
                LOCK.notifyAll();
            }
        }
    }

    public int count() {
        return count;
    }

    public void await() throws InterruptedException {
        synchronized (LOCK) {
            LOCK.wait();
        }
    }

    private static class TestThread implements Runnable {
        private Latch2 latch2;

        public TestThread(Latch2 latch2) {
            this.latch2 = latch2;
        }

        @Override
        public void run() {
            try {
                System.out.println("waiting ..." + Thread.currentThread().getName());
                latch2.await();
                System.out.println("Running ..." + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
