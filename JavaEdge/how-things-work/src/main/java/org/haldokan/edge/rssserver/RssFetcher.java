package org.haldokan.edge.rssserver;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Created by haytham.aldokanji on 9/23/15.
 */
public class RssFetcher {
    private final String channel;
    private Random rand = new Random();

    public RssFetcher(String channel) {
        this.channel = channel;
    }

    public FetchedData fetch() {
        FetchedData data = new FetchedData();
        if (rand.nextInt(5) == 4) {
            data.setFailed(true);
        } else {
            data.addItem(new Item("item" + rand.nextInt(50), "En premier lieu, nous concevons le <<moi>>, et nous y attachons", channel));
            data.addItem(new Item("item" + rand.nextInt(50), "Puis nous concevons le <<mien>>, et nous attachons au monde materiel", channel));
            data.addItem(new Item("item" + rand.nextInt(50), "Comme l'eau captive de la roue du moulin, nous tournons en rond, impuissants", channel));
            data.addItem(new Item("item" + rand.nextInt(50), "Je rend hommage a la compassion qui embrasse tous les etres", channel));
            data.setNext(LocalTime.now().plus(2, ChronoUnit.SECONDS));
        }
        return data;
    }
}
