package org.haldokan.edge.interviewquest.amazon;
/*
 * My solution to an Amazon interview question
 * The Question 4-STAR
 * Design twitter trending topics feature to show trending topics in past 24 hours.
 * 07/12/20
 */
public class TwitterTrending {
    // Tweet will have the number of retweets which determines if it is trending or not
    // Set a minimum threshold on the number of retweets on a tweet to be included in the trending logic
    // Tweet ids are inserted into a min-heap based on the number of retweets. We can limit the size of the heap to a certain value SIZE
    // Min-Heap on retweets:
    // Heap has room: when retweet comes we check if it exists in the heap and we remove it and insert it with the new retweet value. If new we just insert it
    // Heap Has no room: when retweet comes twe check if it exist in the heap and we remove it and insert it with the new retweet value. If new we check if
    // it has more retweets than the tweet at the top (min-heap). If it does we remove the heap top an add the new tweet. If it does not we ignore it.
    // For both cases above (with or w/o room) when a retweet comes we check that original tweet date-time is within the last 24 hours. If it is not we ignore the retweet.
    // Min-heap on tweet date-time
    // When a retweet is inserted into the heap above for the first time we insert the actual tweet date-time into a min-heap based on the date-time.
    // We run a scheduled process that scans the date-time min-heap and remove the entries older than 24 hours from both heaps.
}
