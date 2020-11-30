package org.haldokan.edge.interviewquest.microsoft;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * My solution to a microsoft interview question - Not sure if this was meant as a system design question... I dealt with it as
 * a merely datastructure problem. The solution enables adding and getting photos in O(1) time complexity. Space complexity is
 * relative to the number of photos added. Array length is the number of seconds in 1 year and (I think) java does not allocate
 * space in arrays to non-initialized objects (not actually sure how the jvm deals with that).
 *
 * The Question: 4-STAR
 *
 * Build a data structure to maintain a set of photographs. Your photograph database should allow you to
 * insert and search for photographs, as well as to designate some of the photographs as favourites by marking them.
 * In more detail, your data structure should support the following operations:
 * <p>
 * Insert(x, t, m): inserts photograph x that was taken at time t. If m = 1, then the photograph is marked as a favourite;
 * if m = 0, then it is not a favourite.
 * <p>
 * Search(t): find the next photograph taken after time t.
 * <p>
 * NextFavorite(t): find the next photograph taken after time t that is a favourite.
 * <p>
 * Give a data structure for solving this problem, and explain how it works. For more efficiency, your data structure should
 * be both time and space efficient: more points will be given for more efficient solutions. (Remember that the photographs
 * themselves may be quite large.) Give the performance (i.e., running time) of each operation and the space usage of the data
 * structure. You may assume that at any given time t, your camera has taken at most one photograph x. Describe your solution
 * in words, or very high-level pseudocode. You do not need to provide complete details. You must provide enough detail, however,
 * that your solution is clear.
 * 11/29/20
 */
public class DesignDatastructureForInsertAndSearchAndFavorite {
    // photos for a length of 1 year to the resolution of 1 second. We can generalize by having a map keyed on the year:
    // Map<year, PhotoContainer[]> but I am not going to do that here since it does not add to the basic approach of this solution
    private final PhotoContainer[] photos = new PhotoContainer[366 * 24 * 3600];

    public static void main(String[] args) {
        DesignDatastructureForInsertAndSearchAndFavorite driver = new DesignDatastructureForInsertAndSearchAndFavorite();
        driver.test();
    }

    // O(1)
    public void addPhoto(Photo photo, boolean favorite) {
        int photoTimeToTheSecond = photoTimeToIndex(photo.getTime());
        PhotoContainer container = photos[photoTimeToTheSecond];
        if (container == null) {
            container = PhotoContainer.create();
            photos[photoTimeToTheSecond] = container;
        }
        container.addPhoto(photo, favorite);
    }

    //O(1): will sweep the array for a max of 366 * 24 * 3600 regardless of the number of photos taken (on average much less)
    public Optional<Photo> getPhoto(LocalDateTime time, boolean favorite) {
        int photoTime = photoTimeToIndex(time);
        for (int i = photoTime; i >= 0; i--) {
            PhotoContainer container = photos[i];
            if (container != null) {
                Optional<Photo> photo = container.getPhoto(favorite);
                if (photo.isPresent()) {
                    return photo;
                }
            }
        }
        return Optional.empty();
    }

    private int photoTimeToIndex(LocalDateTime time) {
        return (int) time.toEpochSecond(ZoneOffset.UTC) % photos.length;
    }

    private static final class PhotoContainer {
        private final ListMultimap<Boolean, Photo> container = ArrayListMultimap.create();

        public static PhotoContainer create() {
            return new PhotoContainer();
        }

        public void addPhoto(Photo photo, boolean favorite) {
            container.put(favorite, photo);
        }

        public Optional<Photo> getPhoto(boolean favorite) {
            if (container.containsKey(favorite)) {
                List<Photo> photos = container.get(favorite);
                return Optional.of(photos.get(photos.size() - 1)); // return the very last photo taken at that second
            }
            return Optional.empty();
        }
    }

    private static final class Photo {
        private final String name;
        private final LocalDateTime time;
        private final byte[] payload;

        public Photo(String name, LocalDateTime time, byte[] payload) {
            this.name = name;
            this.time = time;
            this.payload = payload;
        }

        public LocalDateTime getTime() {
            return time;
        }

        public String getName() {
            return name;
        }

        public byte[] getPayload() {
            return payload;
        }

        @Override
        public String toString() {
            return "Photo{" +
                    "name='" + name + '\'' +
                    ", time=" + time +
                    ", payload=" + Arrays.toString(payload) +
                    '}';
        }
    }

    public void test() {
        LocalDateTime time = LocalDateTime.now();
        Photo photo1 = new Photo("beavis", time, new byte[1]);
        addPhoto(photo1, true);

        Photo photo2 = new Photo("butthead", time.plusMinutes(1), new byte[1]);
        addPhoto(photo2, false);

        LocalDateTime time1 = time.plusMinutes(5);
        System.out.println(getPhoto(time1, true));
        System.out.println(getPhoto(time1, false));

        // these should find no photos
        System.out.println(getPhoto(time.minusSeconds(1), true));
        System.out.println(getPhoto(time.minusSeconds(1), false));
    }
}
