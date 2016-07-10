package org.haldokan.edge.interviewquest.bloomberg;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question - for the plotter I used a min heap but since the same ticker can
 * be added to the heap more than one time we have to find the ticker first which makes heap add/remove O(n) time
 * The Question: 4_STAR
 * <p>
 * Implement a tick server the has multiple clients interested in different tickers. Clients have Plotters that are updated
 * in real-time with the top 10 tickers that have the most price updates on the top. What data structure would you choose
 * for the server and client plotters?
 * <p>
 * Created by haytham.aldokanji on 5/14/16.
 */
public class TickServerWithPlotterClients {
    private final Set<ClientRegistration> clientRegistrations;

    public TickServerWithPlotterClients() {
        // so we don't have to synchronize the onTick method
        clientRegistrations = new CopyOnWriteArraySet<>();
    }

    public static void main(String[] args) {
        TickServerWithPlotterClients driver = new TickServerWithPlotterClients();
        driver.testPlotter();
    }

    private void testPlotter() {
        Plotter plotter = new Plotter(5);
        Tick tick1 = new Tick("stock1", 1.2d, LocalDateTime.now());
        Tick tick2 = new Tick("stock2", 1.3d, LocalDateTime.now());
        Tick tick3 = new Tick("stock3", 1.4d, LocalDateTime.now());
        Tick tick4 = new Tick("stock4", 1.5d, LocalDateTime.now());
        Tick tick5 = new Tick("stock5", 1.6d, LocalDateTime.now());
        Tick tick6 = new Tick("stock6", 1.7d, LocalDateTime.now());
        Tick tick7 = new Tick("stock7", 1.8d, LocalDateTime.now());

        for (int i = 0; i < 50; i++) {
            plotter.onTick(tick1);

            plotter.onTick(tick2);
            plotter.onTick(tick2);

            plotter.onTick(tick3);
            plotter.onTick(tick3);
            plotter.onTick(tick3);

            plotter.onTick(tick4);
            plotter.onTick(tick4);
            plotter.onTick(tick4);
            plotter.onTick(tick4);

            plotter.onTick(tick5);
            plotter.onTick(tick5);
            plotter.onTick(tick5);
            plotter.onTick(tick5);
            plotter.onTick(tick5);

            plotter.onTick(tick6);
            plotter.onTick(tick6);
            plotter.onTick(tick6);
            plotter.onTick(tick6);
            plotter.onTick(tick6);
            plotter.onTick(tick6);

            plotter.onTick(tick7);
            plotter.onTick(tick7);
            plotter.onTick(tick7);
            plotter.onTick(tick7);
            plotter.onTick(tick7);
            plotter.onTick(tick7);
            plotter.onTick(tick7);

            Queue<TickCount> heap = plotter.heapSnapshot();
            assertThat(heap.remove().tick, is(tick3));
            assertThat(heap.remove().tick, is(tick4));
            assertThat(heap.remove().tick, is(tick5));
            assertThat(heap.remove().tick, is(tick6));
            assertThat(heap.remove().tick, is(tick7));
            assertThat(heap.isEmpty(), is(true));
        }
    }

    public void addRegistrations(ClientRegistration... registrations) {
        Arrays.stream(registrations).forEach(registration -> {
            clientRegistrations.add(registration);
            registration.run();
        });
    }

    public void removeRegistrations(String... ids) {
        Arrays.stream(ids).forEach(id -> {
            Optional<ClientRegistration> registration = clientRegistrations.stream().findAny();
            if (registration.isPresent()) {
                registration.get().stop();
                clientRegistrations.remove(registration.get());
            }
        });
    }

    public void onTick(Tick tick) {
        clientRegistrations.stream().forEach(registration -> registration.onTick(tick));
    }

    private static class ClientRegistration {
        private final String id;
        private final Set<String> stocks;
        BlockingDeque<Tick> ticks;
        private volatile boolean keepOnRunning;

        public ClientRegistration(String id) {
            this.id = id;
            this.stocks = new HashSet<>();
            ticks = new LinkedBlockingDeque<>();
        }

        public void run() {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                while (keepOnRunning) {
                    try {
                        processTick(ticks.take());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // this is not enough as reading might be blocked; need another thread; poison pill, etc.
        public void stop() {
            keepOnRunning = false;
        }

        public void addStocks(String... stocks) {
            Arrays.stream(stocks).forEach(this.stocks::add);
        }

        public void removeStocks(String... stocks) {
            Arrays.stream(stocks).forEach(this.stocks::remove);
        }

        // blocking queues can slow things down but I don't see an alternative
        public void onTick(Tick tick) {
            if (this.stocks.contains(tick.stock)) {
                ticks.add(tick);
            }
        }

        private void processTick(Tick tick) {
            System.out.printf("%s%n", tick);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClientRegistration that = (ClientRegistration) o;

            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    private static class Tick {
        private final String stock;
        private final double price;
        private final LocalDateTime time;

        public Tick(String stock, double price, LocalDateTime time) {
            this.stock = stock;
            this.price = price;
            this.time = time;
        }

        @Override
        public String toString() {
            return "Tick{" +
                    "stock='" + stock + '\'' +
                    ", price=" + price +
                    ", time=" + time +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tick tick = (Tick) o;

            return stock.equals(tick.stock);

        }

        @Override
        public int hashCode() {
            return stock.hashCode();
        }
    }

    private static class TickCount {
        private final Tick tick;
        private int count;

        public TickCount(Tick tick, int count) {
            this.tick = tick;
            this.count = count;
        }

        public void increment() {
            count++;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TickCount tickCount = (TickCount) o;

            return tick.equals(tickCount.tick);

        }

        @Override
        public int hashCode() {
            return tick.hashCode();
        }
    }

    private static class Plotter {
        private final Queue<TickCount> minHeap;
        private final Map<String, Integer> updateCountByStock = new HashMap<>();
        private final int topSampleSize;

        public Plotter(int topSampleSize) {
            this.topSampleSize = topSampleSize;
            minHeap = new PriorityQueue<>(1024, (t1, t2) -> t1.count - t2.count);
        }

        public void onTick(Tick tick) {
            updateCountByStock.compute(tick.stock, (k, v) -> v == null ? 1 : v + 1);
            int count = updateCountByStock.get(tick.stock);

            Optional<TickCount> potentialTick = minHeap.stream()
                    .filter(tickCount -> tickCount.tick.equals(tick))
                    .findAny();

            if (potentialTick.isPresent()) {
                TickCount tickCount = potentialTick.get();
                tickCount.increment();
                minHeap.remove(tickCount);
                minHeap.add(tickCount);
            } else if (minHeap.size() < topSampleSize || count > minHeap.peek().count) {
                TickCount tickCount = new TickCount(tick, count);
                minHeap.add(tickCount);
                if (minHeap.size() > topSampleSize) {
                    minHeap.remove();
                }
            }
        }

        public Queue<TickCount> heapSnapshot() {
            return new PriorityQueue<>(minHeap);
        }
    }
}
