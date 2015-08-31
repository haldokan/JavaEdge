package org.haldokan.edge.interviewquest.google.imageserver;

import java.time.LocalTime;

/**
 * Created by haytham.aldokanji on 8/31/15.
 */
public class ImageAccess {
    private final String imageId;
    private final LocalTime accessTime;

    public ImageAccess(String imageId, LocalTime accessTime) {
        this.imageId = imageId;
        this.accessTime = accessTime;
    }

    public String getImageId() {
        return imageId;
    }

    public LocalTime getAccessTime() {
        return accessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageAccess that = (ImageAccess) o;

        return imageId.equals(that.imageId);
    }

    @Override
    public int hashCode() {
        return imageId.hashCode();
    }

    @Override
    public String toString() {
        return "ImageAccess{" +
                "imageId='" + imageId + '\'' +
                ", accessTime=" + accessTime +
                '}';
    }
}
