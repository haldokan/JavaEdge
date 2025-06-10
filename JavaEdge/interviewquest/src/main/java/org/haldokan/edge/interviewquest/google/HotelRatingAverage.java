package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google interview question - one functional pipe
 * The Question 2_STAR
 * <p>
 * You have score (0-10) of the hotels per user in this format:
 * <p>
 * scores = [
 * {'hotel_id': 1001, 'user_id': 501, 'score': 7},
 * {'hotel_id': 1001, 'user_id': 502, 'score': 7},
 * {'hotel_id': 1001, 'user_id': 503, 'score': 7},
 * {'hotel_id': 2001, 'user_id': 504, 'score': 10},
 * {'hotel_id': 3001, 'user_id': 505, 'score': 5},
 * {'hotel_id': 2001, 'user_id': 506, 'score': 5}
 * ]
 * <p>
 * Any given hotel might have more than one score.
 * <p>
 * Implement a function, get_hotels(scores, min_avg_score) that returns a list of hotel ids that have average score
 * equal to or higher than min_avg_score.
 * <p>
 * get_hotels(scores, 5) -> [1001, 2001, 3001]
 * get_hotels(scores, 7) -> [1001, 2001]
 * <p>
 * Created by haytham.aldokanji on 7/10/16.
 */
public class HotelRatingAverage {

    public static void main(String[] args) {
        HotelRatingAverage driver = new HotelRatingAverage();
        driver.test();
    }

    public List<String> getHotels(List<HotelRating> ratings, double minAverage) {
        return ratings.stream()
                .collect(Collectors.groupingBy(rating -> rating.hotelId,
                        Collectors.averagingDouble(rating -> rating.score)))
                .entrySet().stream()
                .filter(entry -> entry.getValue() >= minAverage)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void test() {
        List<HotelRating> hotelRatings = Lists.newArrayList(
                new HotelRating("1001", "user1", 7d),
                new HotelRating("1001", "user2", 7d),
                new HotelRating("1001", "user3", 7d),
                new HotelRating("2001", "user4", 10d),
                new HotelRating("3001", "user5", 5d),
                new HotelRating("2001", "user6", 5d)
        );
        List<String> hotels = getHotels(hotelRatings, 5d);
        System.out.printf("%s%n", hotels);
        assertThat(hotels.size(), is(3));
        assertThat(hotels, containsInAnyOrder("1001", "2001", "3001"));

        hotels = getHotels(hotelRatings, 7d);
        System.out.printf("%s%n", hotels);
        assertThat(hotels.size(), is(2));
        assertThat(hotels, containsInAnyOrder("1001", "2001"));
    }

    private static final class HotelRating {
        private final String hotelId;
        private final String userId;
        private final double score;

        public HotelRating(String hotelId, String userId, double score) {
            this.hotelId = hotelId;
            this.userId = userId;
            this.score = score;
        }

        @Override
        public String toString() {
            return "HotelRating{" +
                    "hotelId='" + hotelId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}
