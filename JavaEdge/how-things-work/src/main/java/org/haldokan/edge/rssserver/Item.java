package org.haldokan.edge.rssserver;

/**
 * Created by haytham.aldokanji on 9/23/15.
 */
public class Item {
    private final String id, title, channel;

    public Item(String id, String title, String channel) {
        this.id = id;
        this.title = title;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
