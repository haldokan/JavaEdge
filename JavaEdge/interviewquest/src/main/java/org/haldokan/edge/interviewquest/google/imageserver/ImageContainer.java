package org.haldokan.edge.interviewquest.google.imageserver;

import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by haytham.aldokanji on 8/28/15.
 */
public class ImageContainer {
    private final Future<Image> image;
    private LocalTime accessedAt;

    public ImageContainer(Future<Image> image, LocalTime accessedAt) {
        this.image = image;
        this.accessedAt = accessedAt;
    }

    //TODO MUST remove image from cache on exception
    public Image getImage() {
        try {
            return image.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public LocalTime getAccessedAt() {
        return accessedAt;
    }

    public void setAccessedAt(LocalTime accessedAt) {
        this.accessedAt = accessedAt;
    }

    @Override
    public String toString() {
        return "ImageContainer{" +
                "image=" + image +
                ", accessedAt=" + accessedAt +
                '}';
    }
}