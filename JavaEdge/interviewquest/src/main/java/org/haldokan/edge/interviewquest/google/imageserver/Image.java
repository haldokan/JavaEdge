package org.haldokan.edge.interviewquest.google.imageserver;

/**
 * Created by haytham.aldokanji on 9/1/15.
 */
public class Image {
    private final String id;
    private final String tag;
    private final String descr;

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

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", descr='" + descr + '\'' +
                '}';
    }
}
