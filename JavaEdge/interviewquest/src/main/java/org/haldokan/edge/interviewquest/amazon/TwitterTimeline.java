package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * My solution to an Amazon interview question - design and in-process implementation. Finding a tweet from an account
 * is O(log n) where n is the number of tweets from that account. I utilized that fact that tweets from the same account
 * are naturally sorted by the timestamp which I used to identify a tweet: the combination of an account id and the tweet
 * timestamp is the tweet id. Using binary search on the list of tweets that are naturally sorted by the time stamp enables
 * the O(log n) time complexity of finding a tweet. I also used a flavor of the Observer pattern. The solution can
 * be made to use Guava message bus.
 * <p>
 * The Question: 5_STAR
 * <p>
 * Design Twitter Timeline
 * Twitter Timeline displays a stream of Tweets from accounts you have chosen to follow on Twitter. You may see
 * suggested content powered by a variety of signals. You can reply, retweet, or like a Tweet from within the timeline.
 * <p>
 * Created by haytham.aldokanji on 7/25/16.
 */
public class TwitterTimeline {
    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        TwitterTimeline driver = new TwitterTimeline();
        driver.test();
    }

    private void test() {
        for (int i = 0; i < 10; i++) {
            String accountId = "account" + i;
            accounts.put(accountId, new Account(accountId));
        }

        Account account1 = accounts.get("account1");
        account1.addFollowers("account2", "account3", "account4", "account5", "account6");

        Account account2 = accounts.get("account2");
        account2.addFollowers("account1", "account5", "account6", "account7", "account8", "account9");

        Tweet tweet = account1.tweet("Hello!");
        System.out.printf("%s%n", "----------------------");

        account2.retweet(tweet.accountId, tweet.time);
        System.out.printf("%s%n", "----------------------");

        account2.reply(tweet.accountId, tweet.time, "Hello There!");
        System.out.printf("%s%n", "----------------------");

        account2.like(tweet.accountId, tweet.time);
        System.out.printf("%s%n", "----------------------");
    }

    private final static class Account {
        private final String id;
        private final List<String> accountsFollowingMe = new ArrayList<>();
        private final List<String> accountsIamFollowing = new ArrayList<>();
        // sorted naturally by the timestamp that we can use as an id for tweets from an account, alternatively
        // we can maintain another mapping b/w account id and a set of tweets sorted on tweet id
        private final ListMultimap<String, Tweet> tweetsByAccount =
                Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

        public Account(String id) {
            this.id = id;
        }

        public void onTweet(Tweet tweet) {
            System.out.printf("%s@%s -> %s%n", id, tweet.tweetType, tweet); // display

            switch (tweet.tweetType) {
                case TWEET:
                case RETWEET:
                    tweetsByAccount.put(tweet.accountId, tweet);
                    break;
                case REPLY:
                    Tweet originalPayload = tweet.replies.get(0); // this guy has only the account id and time (no text)
                    Tweet original = findTweet(originalPayload.accountId, originalPayload.time);
                    original.addReply(tweet);
                    break;
                case LIKE:
                    originalPayload = tweet.likes.get(0);
                    original = findTweet(originalPayload.accountId, originalPayload.time);
                    original.addLike(tweet);
                    break;
                default:
                    throw new IllegalArgumentException(tweet.tweetType.name());
            }
        }

        public Tweet retweet(String accountId, LocalDateTime tweetTime) {
            Tweet tweet = findTweet(accountId, tweetTime);
            Tweet retweet = Tweet.makeRetweet(id, LocalDateTime.now(), tweet);
            // don't retweet to the originator account
            return doTweet(retweet, accountId);
        }

        public Tweet reply(String accountId, LocalDateTime tweetTime, String text) {
            Tweet reply = Tweet.makeReply(id, text, LocalDateTime.now(), Tweet.makeIdentifier(accountId, tweetTime));
            accounts.get(accountId).onTweet(reply);
            return reply;
        }

        public Tweet like(String accountId, LocalDateTime tweetTime) {
            Tweet like = Tweet.makeLike(id, LocalDateTime.now(), Tweet.makeIdentifier(accountId, tweetTime));
            accounts.get(accountId).onTweet(like);
            return like;
        }

        public Tweet tweet(String text) {
            return doTweet(Tweet.makeTweet(id, text, LocalDateTime.now()));
        }

        public Tweet findTweet(String accountId, LocalDateTime tweetTime) {
            List<Tweet> tweets = tweetsByAccount.get(accountId);
            int index = Collections.binarySearch(tweets, Tweet.makeIdentifier(accountId, tweetTime),
                    (t1, t2) -> t1.time.compareTo(t2.time));

            return tweets.get(index);
        }

        public void addFollowers(String... accountIds) {
            Arrays.stream(accountIds).forEach(accountId -> {
                accountsFollowingMe.add(accountId);
                accounts.get(accountId).accountsIamFollowing.add(id);
            });
        }

        private Tweet doTweet(Tweet tweet, String... excludedAccounts) {
            Set excluded = Sets.newHashSet(excludedAccounts);
            tweetsByAccount.put(id, tweet);

            accountsFollowingMe.stream()
                    .filter(accountId -> !excluded.contains(accountId))
                    .forEach(accountId -> accounts.get(accountId).onTweet(tweet));
            return tweet;
        }
    }

    private final static class Tweet {
        private final List<String> retweetChain = new ArrayList<>();
        private final List<Tweet> replies = new ArrayList<>();
        private final List<Tweet> likes = new ArrayList<>();
        private final String accountId;
        private final String text;
        private final LocalDateTime time;
        private final TweetType tweetType;

        private Tweet(String accountId, String text, LocalDateTime time, TweetType tweetType) {
            this.accountId = accountId;
            this.text = text;
            this.time = time;
            this.tweetType = tweetType;
        }

        public static Tweet makeTweet(String accountId, String text, LocalDateTime time) {
            Tweet tweet = new Tweet(accountId, text, time, TweetType.TWEET);
            tweet.retweetChain.add(accountId);
            return tweet;
        }

        public static Tweet makeRetweet(String accountId, LocalDateTime time, Tweet tweet) {
            Tweet retweet = new Tweet(accountId, tweet.text, time, TweetType.RETWEET);
            retweet.retweetChain.addAll(tweet.retweetChain);
            retweet.retweetChain.add(accountId);

            return retweet;
        }

        public static Tweet makeReply(String accountId, String text, LocalDateTime time, Tweet original) {
            Tweet tweet = new Tweet(accountId, text, time, TweetType.REPLY);
            tweet.addReply(original);
            return tweet;
        }

        public static Tweet makeLike(String accountId, LocalDateTime time, Tweet original) {
            Tweet tweet = new Tweet(accountId, null, time, TweetType.LIKE);
            tweet.addLike(original);
            return tweet;
        }

        public static Tweet makeIdentifier(String accountId, LocalDateTime time) {
            return new Tweet(accountId, null, time, null);
        }

        public void addReply(Tweet reply) {
            replies.add(reply);
        }

        public void addLike(Tweet like) {
            likes.add(like);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tweet tweet = (Tweet) o;
            return accountId.equals(tweet.accountId) && time.equals(tweet.time);
        }

        @Override
        public int hashCode() {
            int result = accountId.hashCode();
            result = 31 * result + time.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Tweet{" +
                    "retweetChain=" + retweetChain +
                    ", replies=" + replies +
                    ", likes=" + likes +
                    ", accountId='" + accountId + '\'' +
                    ", text='" + text + '\'' +
                    ", time=" + time +
                    ", tweetType=" + tweetType +
                    '}';
        }

        private enum TweetType {TWEET, RETWEET, REPLY, LIKE}
    }
}
