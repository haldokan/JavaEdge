package org.haldokan.edge.interviewquest.amazon;

import com.google.inject.internal.cglib.core.$Customizer;

import java.util.*;

/**
 * My rough-edged solution to an Amzaon interview question - calculate the topmost books at login using min-heap that has the sample size
 *
 * The Question: 3.5-STAR
 *
 * Design the Good-reads recommendation model.
 *
 * Goodreads is a "social networking book" website that allows individuals to freely search its database of books.
 *
 * - Any given user has a List of friends.
 * - User can add books to his/her shelve
 * - When a user logs in, we need to show the top 10 books owned by his friends
 *
 * Example:
 * User1 has u2, u3, u4, u5 as friends
 *
 * u2 owns b1, b2, …b10
 * u3 own b1, b2, b11…b20
 * u4 owns b1, b2, b11…b20
 * u5 owns b1, b33, b35, etc
 *
 * topmost book is b1, second topmost book is b2 and so on.
 *
 * If you maintain a cache of top 10 books in the user object for each of the users. How often will you update the cache?
 */
public class DesignGoodReadRecommendationSystem {
    Map<String, User> users;
    int sampleSize;

    List<String> login(String id) {
        User user = users.get(id);

        Map<String, Integer> countByBook = new HashMap<>();
        PriorityQueue<BookCount> topmost = new PriorityQueue<>(Comparator.comparingInt(bc -> -bc.count));

        for (User friend : user.friends) {
            for (String book : friend.books) {
                int newCount = countByBook.compute(book, (b, count) -> count == null ? 1 : count + 1);
                BookCount bookCount = new BookCount(book, newCount);

                if (topmost.size() < sampleSize || topmost.contains(bookCount)) { // containment is based on equality on book id
                    topmost.remove(bookCount); // will be found based on book id (count is not part of equality check)
                    topmost.add(bookCount); // added with new count
                } else if (bookCount.count > topmost.peek().count) { // add if new book with count that is greater than the book at the top of the min heap
                    topmost.remove(); // remove top
                    topmost.add(bookCount);
                }
            }
        }

        List<String> topBooks = new LinkedList<>(); // linked-list is better than array-list at inserting at tail of the list
        while (!topmost.isEmpty()) {
            topBooks.add(0, topmost.remove().id); // this will inverse the order to max count first
        }
        return topBooks;
    }

    static class BookCount {
        String id;
        int count;

        public BookCount(String id, int count) {
            this.id = id;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BookCount bookCount = (BookCount) o;
            return id.equals(bookCount.id); // equality on id only
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    static class User {
        String id;
        List<User> friends;
        List<String> books;
    }
}
