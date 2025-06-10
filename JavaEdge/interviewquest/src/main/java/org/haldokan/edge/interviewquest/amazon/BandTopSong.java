package org.haldokan.edge.interviewquest.amazon;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * NOTE: this can also be solved using PriorityQueue trading time complexity for space complexity
 * My solution to an Amazon interview question
 * The Question: 3_STAR + 1/2
 * <p>
 * A radio station calls your webservice using api: played(bandname, songname). Users of your webservice want to query for
 * for a band's top song using api topSong(bandname).
 * Implement the API to answer the query in O(1)
 * <p>
 * Created by haytham.aldokanji on 7/26/16.
 */
public class BandTopSong {
    private final Map<String, Map<String, Integer>> playedSongs = new HashMap<>();
    private final Map<String, String> bandTopSong = new HashMap<>();
    private final Map<String, Integer> bandTopRating = new HashMap<>();

    public static void main(String[] args) {
        BandTopSong driver = new BandTopSong();
        driver.test();
    }

    public void played(String bandName, String songName) {
        Map<String, Integer> playedSong = playedSongs.computeIfAbsent(bandName, s -> new HashMap<>());
        Integer rating = playedSong.compute(songName, (k, v) -> v == null ? 1 : v + 1);

        Integer currentTopRating = bandTopRating.get(bandName);
        Integer topRating = bandTopRating.compute(bandName, (k, v) -> v == null ? rating : Math.max(rating, v));

        if (currentTopRating == null || topRating > currentTopRating) {
            bandTopSong.put(bandName, songName);
        }
    }

    public String topSong(String bandName) {
        return bandTopSong.get(bandName);
    }

    private void test() {
        for (int i = 0; i < 10; i++) {
            played("band1", "song1");
            if (i < 9) {
                played("band1", "song2");
            }
        }

        for (int i = 0; i < 10; i++) {
            played("band2", "song3");
            if (i < 9) {
                played("band2", "song4");
            }
        }
        System.out.printf("%s%n", playedSongs);
        System.out.printf("%s%n", bandTopRating);
        System.out.printf("%s%n", bandTopSong);

        assertThat(topSong("band1"), is("song1"));
        assertThat(topSong("band2"), is("song3"));
    }

}
