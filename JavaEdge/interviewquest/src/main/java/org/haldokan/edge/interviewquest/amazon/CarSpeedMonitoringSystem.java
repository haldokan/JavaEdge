package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 * We have cameras installed all over the city to detect speeding. The cameras snap pictures of the plates of speeding
 * cars and feed the data into the system that keep track of violations.
 * Design such a system.
 * <p>
 * The Question: 4_STAR
 * <p>
 * Created by haytham.aldokanji on 8/13/16.
 */
public class CarSpeedMonitoringSystem {
    /**
     * 1- The data channeled from the cameras to the system will include a picture of the plate, speed, time, and location
     *
     * 2- We can use a data store similar to Big Table, HBase or Cassandra to store the data keyed and sorted by the plate
     * number. Since the same car can have multiple speed violations each plate number key will point to different
     * timestamped-versions of the speed, and location data (read how Big Table or Cassandra tag data for the same key
     * with the timestamp).
     *
     * 3- Having the data keyed and sorted by the plate number enables users to pull the last speed violation (last timestamp)
     * or all of them in O(1) from the data store. But it also enables (thanks to the sorted keys) answering range
     * queries efficiently (log n): for example query for speed violations for cars from other states that happened to be in the city.
     *
     * 4- Users will want to know of the spots in the city where most speed violations occur and in what time ranges. For
     * that we will need another table in the data store keyed by location that has the number of violations versioned by
     * the timestamp. We can do aggregations on the data in this table based on location, time, and number of violations.
     *
     * 5- Speed violation data can be sent from the cameras to web applications via http requests. But I think using RPC
     * would be preferred for performance reasons (for one requests have less auxiliary data like headers, etc.). RPC is
     * the protocol used for high frequency and throughput scenarios. For example clients to Google internal streaming app
     * MillWheel (open source equivalents Storm or Spark) use RPC to stream data in to the system.
     *
     * 6- We have to accommodate high throughput from the cameras thus we need multiple instances of the web apps running
     * on different servers. We use a load balancer to distribute the load. Load balancer can be configured to distribute
     * the load based on location ranges but when cpu or memory pressure is detected on a server (using standard server monitoring
     * processes) the load balancer can then adjust the location range assignment to server dynamically.
     *
     * 7- In a high frequency scenario we cannot update the data store on each request from the cameras because that will
     * create latency and possibly lead to failed/timed-out requests. NOSQL data stores can handle that better because they
     * normally do a fast write to a commit log and update   a memory cache that is backed to disk when it grows big.
     *
     * 8- In order to improve application responsiveness to multiple users we can keep most recent speed violation data in
     * a memory cache. For example an LRU cache that keeps data for the last 24 hours.
     *
     * 9- The statistics we collect about the violations per locations over time can be aged and removed. Cassandra for example
     * provides the option of setting TTL on each row of data. A background sweeper process removes aged data.
     *
     * 10- We are collecting large amount of data so we should do data compaction to reduce size. Having stored the data sorted
     * by the key, compacting it is usually more effective: for example if all New Jersey plates starts with NJ-GS-7A,
     * zipping the data will store this part of the plate number in a single entry in a map.
     *
     * 11- Users may want to know in real-time of the hot spots of speed violations (item 4). They don't want to run a job
     * that may take a long time to query the data store for this info. We can support that using stream processing systems
     * like Storm or Spark. As requests come from the cameras they are sorted out by location and sent out to processing
     * nodes (called bolts in Storm). Each node counts violations for a single location over a sliding window of let's say
     * an hour. All these counting nodes report their counts to a reducing node that simply sort the reported data based
     * on the number of violations and stores the top 10 (storage can be in memory or disk). Monitoring apps can query the
     * reducing node any time to get the top offenders over the last hour or what have you. Or we can have the reducing
     * node push the data to subscribing apps and components.
     *
     */
}
