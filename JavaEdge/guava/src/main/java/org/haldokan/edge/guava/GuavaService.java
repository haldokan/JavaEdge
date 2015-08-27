package org.haldokan.edge.guava;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuavaService extends AbstractExecutionThreadService {
    private static final String POISON = new String();
    private static final String ABORT = new String();
    private BlockingQueue<String> bq = new ArrayBlockingQueue<>(1000);
    private String sid;

    public GuavaService(String sid) {
        super();
        this.sid = sid;
    }

    public static void main(String[] args) throws Exception {
        List<Service> sl = new ArrayList<>();
        GuavaService gs1 = new GuavaService("sid1");
        GuavaService gs2 = new GuavaService("sid2");

        // the es can be passed to all listers
        // ExecutorService exs = Executors.newFixedThreadPool(5);
        ListeningExecutorService exs = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));

        gs1.addListener(new GuavaServiceListener(gs1, new ICompletionListener() {
            @Override
            public void onSuccess(String s) {
                System.out.println("sucess->" + s);
                // call CountingLatch.countDown()
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("failure->" + e.getMessage());
                // // call CountingLatch.countDown()

            }
        }), exs);
        gs2.addListener(new GuavaServiceListener(gs2, new ICompletionListener() {

            @Override
            public void onSuccess(String s) {
                System.out.println("success->" + s);

            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("failure->" + e.getMessage());

            }
        }), exs);

        sl.add(gs1);
        sl.add(gs2);
        ServiceManager sm = new ServiceManager(sl);
        sm.addListener(new ServiceManager.Listener() {
            @Override
            public void failure(Service service) {
                Multimap<State, Service> sbs = sm.servicesByState();
                System.out.println("sbs->" + sbs);
                String newStates = FluentIterable.from(sbs.keySet()).filter(new Predicate<State>() {
                    @Override
                    public boolean apply(State state) {
                        return !(state == State.TERMINATED || state == State.FAILED);
                    }
                }).transform(new Function<State, Set<State>>() {
                    @Override
                    public Set<State> apply(State s) {
                        Collection<Service> sc = sbs.get(s);
                        Set<State> ns = new HashSet<>();
                        for (Service sr : sc) {
                            // not the when a service is aborted from
                            // here this listener will be called again
                            ((GuavaService) sr).abort();
                            ns.add(sr.state());
                        }
                        return ns;
                    }
                }).toString();
                System.out.println("new states->" + newStates);
                // force shutdown the listener threads - no guarranty they will
                // shutdown (read shutdownNow docs).
                // System.out.println("es.shutdownNow->" + es.shutdownNow());
            }
            // direct executor is the same thread the ServiceManager runs in
            // (equivalent to not passing an executor)
        }, MoreExecutors.directExecutor());

        sm.startAsync();
        System.out.println(gs1.state());

        sm.awaitHealthy();
        System.out.println(gs1.state());
        System.out.println(gs2.state());

        for (int i = 0; i < 100; i++) {
            gs1.qu("A" + i);
            gs2.qu("B" + i);
            if (i == 40)
                gs1.stopAsync();
            if (i == 50) {
                // gs2.qu(ABORT);
                gs2.stopAsync();
                System.out.println(gs1.state());
                System.out.println(gs2.state());
            }
        }
        Thread.sleep(1000);
        System.out.println(sm.servicesByState());

        sm.stopAsync();
        sm.awaitStopped();
        System.out.println("service states->" + sm.servicesByState());

        exs.shutdown();
        System.out.println(exs.awaitTermination(5, TimeUnit.SECONDS));
    }

    public String getSid() {
        return sid;
    }

    public void qu(String s) throws InterruptedException {
        if (state() == State.RUNNING) {
            System.out.println("put->" + s);
            bq.put(s);
        }
    }

    @Override
    protected void run() throws Exception {
        String s;
        while ((s = bq.take()) != POISON) {
            System.out.println("take->" + s);
            if (s == ABORT)
                throw new RuntimeException("XXX");
        }
    }

    @Override
    protected void triggerShutdown() {
        while (true) {
            try {
                bq.put(POISON);
                break;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }
        }
    }

    public void abort() {
        System.out.println("Aborting service" + this);
        while (true) {
            try {
                bq.put(ABORT);
                break;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }
        }
    }

    @Override
    public String toString() {
        return "GuavaService [bq=" + bq + ", sid=" + sid + "]";
    }

    private static class GuavaServiceListener extends Service.Listener {
        private final GuavaService gs;
        private final ICompletionListener cl;

        public GuavaServiceListener(GuavaService gs, ICompletionListener cl) {
            this.gs = gs;
            this.cl = cl;
        }

        @Override
        public void terminated(State from) {
            super.terminated(from);
            System.out.println("status:service:" + gs.getSid() + "->" + gs.state());
            for (int i = 0; i < 10; i++) {
                System.out.println("gs.sid/state(" + i + ")" + gs.getSid() + "/" + gs.state());
                if (i == 5) {
                    RuntimeException rte = new RuntimeException("ZZZ");
                    cl.onFailure(rte);
                    throw rte;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
            cl.onSuccess("Voila!");
        }
    }

}
