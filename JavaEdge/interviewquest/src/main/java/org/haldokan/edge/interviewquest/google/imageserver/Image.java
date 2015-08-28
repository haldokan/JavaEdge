package org.haldokan.edge.interviewquest.google.imageserver;

import java.time.LocalTime;

/**
 * Created by haytham.aldokanji on 8/28/15.
 */
public class Image {
    private String id;
    private String tag;
    private String descr;
    private LocalTime loadedAt;

    public Image(String id, String tag, String descr) {
        this.id = id;
        this.tag = tag;
        this.descr = descr;
    }

    public String getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getDescr() {
        return descr;
    }

    public void setLoadedAt(LocalTime time) {
        this.loadedAt = time;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", descr='" + descr + '\'' +
                ", loadedAt=" + loadedAt +
                '}';
    }
}