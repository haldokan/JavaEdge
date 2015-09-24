package org.haldokan.edge.rssserver;

import java.util.concurrent.*;

/**
 * Created by haytham.aldokanji on 9/24/15.
 */
public class RssServer {
    private static final Item POISON_PILL = new Item(null, null, null);

    private final BlockingQueue<Item> mergeQu;
    private final String[] channels;
    private final ExecutorService executorService;
    private final RssService[] rssServices;

    public RssServer(String... channels) {
        this.mergeQu = new LinkedBlockingQueue<>();
        this.channels = channels;
        this.rssServices = new RssService[channels.length];
        this.executorService = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) throws InterruptedException {
        RssServer server = new RssServer("Java", "Pyhton", "GO", "Javascript", "Scala");
        new Thread(() -> server.start()).start();
        TimeUnit.SECONDS.sleep(10);
        for (RssService service : server.rssServices) {
            service.stop();
        }
        server.mergeQu.offer(RssServer.POISON_PILL);
    }

    public void start() {
        for (int i = 0; i < channels.length; i++) {
            rssServices[i] = new RssService(new RssFetcher(channels[i]), 100, mergeQu);
            executorService.submit(rssServices[i]::start);
        }
        for (; ; ) {
            try {
                Item item = mergeQu.take();
                if (item == POISON_PILL) {
                    System.out.println("RSS server quitting");
                    break;
                }
                System.out.println(item);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
