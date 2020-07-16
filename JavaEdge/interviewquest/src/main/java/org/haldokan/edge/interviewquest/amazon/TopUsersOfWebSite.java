package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 *
 * The Question: 3.5-STAR
 *
 * A startup website has a lot of real-time traffic . I want to see the real-time view (refreshed every 1 min) of top 20
 * users by hit count within last 10 mins. Full distributed system, I have to resolve all the concurrency issues.
 *
 * 07/15/20
 */
public class TopUsersOfWebSite {
    // min-heap on number hits { user: { id, numOfhits, hitTime } } - logic to keep heap size to the number of top users \
    // and to update existing users is done elsewhere in this repository for similar problems.
    // min-heap on hit time { user: { id, hitTime } }. Run back-end process to remove entries in the heap older than T minutes \
    // same entries are removed from the hits heap
    // shard users based on their Ids to multiple-servers using consistent \
    // hashing (to provide for adding/removing servers and having to re-distribute only numberOfEntries/numberOfServers entries)
    // each shard-server does the above and submits the min-heap on user hits to an aggregating server that adds them to \
    // a sorted data structure (BST for instance) based on number of hits. Alternative they can be added to a max heap on number of hits
}
