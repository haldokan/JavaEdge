package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 * <p>
 * The Question: 3_STAR + 1/2
 * <p>
 * Design a system that processes an infinite stream of data:
 * - each record comes as a tuple(url, html content)
 * - extract and store the occurrences of:
 * urls, hosts, top-level-domains, in/out links of the page
 * <p>
 * We have one machine that has enough disk space but limited memory.
 * <p>
 * What data structures as building blocks would you use?
 * <p>
 * Created by haytham.aldokanji on 8/12/16.
 */
public class DesignePersisterOfInfiniteDataStream {
    /**
     * 1- Since we have limited memory and the html content can be arbitrarily large we do not want to queue the incoming
     * data. We rather want to parse the needed parts and throw away the rest of each tuple.
     *
     * 2- We want to parse the tuples as soon as we receive them. Since there is no ordering involved we can do the
     * processing in parallel.
     *
     * 3- We create a thread pool that is used by the stream receiver. Once a tuple is received it is submitted to a thread
     * from the pool where it is parsed and put on one of several blocking queues.
     *
     * 4- To reduce write and read contention on the blocking queues we adopt a mechanism to distribute the parsed tuples to
     * several queues based on url lexicographical ranges: For example urls the start with A to H go to queue1, urls that
     * start with H to M go to queue2, and so forth.
     *
     * 5- We have another thread pool that act as consumer to the queues that have the tuples. Consumption is done in parallel
     * and the tuples are written to disk. We don't write individual records to the disk we rather buffer them to certain
     * sizes and batch write them to disk.
     *
     * 6- We write the different queues to different files on different drives to reduce contention on the disk.
     *
     * 7- We configure the writers to disk to start writing to new files once the file sizes on disk get to certain size so
     * we effectively close files for writing periodically.
     *
     * 8- we run processes that merge the closed files into bigger files.
     *
     */
}
