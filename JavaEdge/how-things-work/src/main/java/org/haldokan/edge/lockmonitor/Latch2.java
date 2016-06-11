package org.haldokan.edge.lockmonitor;

/**
 * Latch implementation using wait and notify
 * The Question: 3_STAR
 * @author haldokan
 */
public class Latch2 {
    private static final Object LOCK = new Object();
    private int count;

    public Latch2(int count) {
        this.count = count;
    }

    public static void main(String[] args) throws InterruptedException {
        int lcount = 3;
        Latch2 l = new Latch2(lcount);
        Thread t1 = new Thread(new TestThread(l));
        Thread t2 = new Thread(new TestThread(l));

        t1.start();
        t2.start();

        Thread.sleep(3000);
        // we can count down as low as we want
        for (int i = 0; i < lcount + 7; i++) {
            l.countdown();
            System.out.println("counting down..." + l.count());
        }
    }

    public void countdown() {
        synchronized (LOCK) {
            count--;
            LOCK.notifyAll();
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
        private Latch2 l;

        public TestThread(Latch2 l) {
            this.l = l;
        }

        @Override
        public void run() {
            try {
                System.out.println("waiting ..." + Thread.currentThread().getName());
                l.await();
                System.out.println("Running ..." + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
