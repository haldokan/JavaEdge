package org.haldokan.edge.interviewquest.bloomberg;

/**
 * My solution to a Bloomberg interview question
 * <p>
 * The Question: 4_STAR
 * <p>
 * A client wants to build a software phone book that contains everyone in the world (7 billion people). Every person
 * has a name and the name is unique. What data structure would you use to store the data?
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class PhoneBookForThePlanetPopulation {
    /**
     * 7 billion people: Lets assume that a person name can fit in 30 chars which require 30 * 2 = 60 bytes per
     * name. The planet population will require 7 * 10^9 * 60 or 420 gigabytes. Further we have
     * to store the phone numbers: Let's assume that we can store a number in 15 chars which yields another 210 gigabytes in total
     * for the planet. So in total we need 420 + 210 = 630 gigabytes in total. Memories even on ultra modern servers are
     * not big enough for such a load.
     *
     * Data has to be stored on disk with efficient memory representation coupled with caching.
     *
     * Here is a solution inspired by a seminal Google research paper on Big Table:
     *
     * - Key values (name:phone number) are stored on disk sorted in lexicographical order based on 'name'.
     * - Divide keys (names) into different ranges (A->H, I->M, etc). Each ranges is stored in a separate file
     * - We need a distributed file system a la GFS or Hadoop so the file system does not become a bottle neck
     * - Create metadata files that have info on which files have which key ranges
     * - Have a master server assign the different key range files to different "work" servers to serve the data to client apps
     * - The work servers can employ partial or total caching in memory of the name:number key/value.
     * - Caching in memory can be done using sorted hash maps (data is already sorted on disk) to answer single value and range queries.
     * - The work server can seek key ranges on disk in O(log n) since names are sorted lexicographically on disk. Same can be done in memory too.
     * - Client requests hit the master server first which find the work server that serves the name range specified in the request
     * - Optionally, instead of using the master server to route requests we can use a client library that downloads the metadata files to the client
     */
}
