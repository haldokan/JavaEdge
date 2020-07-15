package org.haldokan.edge.interviewquest.amazon;

/**
 * My solution to an Amazon interview question
 * The Question: 4-STAR
 * Design a distributed key-value store that can perform the following
 * Store a set of attributes (value) against a particular key (k)
 * Fetch the value stored against a particular key (k)
 * Delete a key (k)
 * - Perform a secondary index scan to fetch all key along with their attributes where one of the attribute values is v.
 * Key can have a value consisting of multiple attributes.
 * Each attribute will have name, type associated (primitive types - boolean, double, integer, string) & type has to be identified at run time.
 *
 * Ex -
 * 1) Key = delhi has 2 attributes ( pollution_level & population)
 * 2) Key = jakarta has 3 attributes (latitude, longitude, pollution_level)
 * 3) Key = bangalore has 4 attributes (extra - free_food)
 * 4) Key = india has 2 attributes (capital & population)
 * 5) Key = crocin has 2 attributes (category & manufacturer)
 *
 * Example of Secondary index:
 * Get all keys (cities) where pollution_level is high. Get all medicines by manufacturer (GSK)
 * So, in a nutshell, value must be strongly typed when defined.
 *
 * Attribute
 * 1. Attribute is uniquely identified by its name (latitude, longitude etc.
 * 2. Data type of the attribute is defined at the first insert. (i.e. data type of pollution_level is set when key = delhi is inserted)
 * 3. Once data type is associated with a particular attribute, it cannot be changed.
 * (i.e. free_food when defined takes type = boolean, hence, any key when using the attribute - free_food must allow only boolean values on subsequent inserts/updates)
 *
 * Non-functional requirements
 *
 * Highly scalable - Support for high throughput with very low latency
 * Highly available
 * Shared nothing architecture i.e. Support for Multiple nodes and each node is independent & self-sufficient.
 * Stretch - Smart client i.e. clients being aware of available servers & makes smart routing based on that available information.
 *
 * 07/12/20
 */
public class DesignKeyValueStore {
    // Map<Key, Map<attribute-name, attribute-value>>
    // Map<attribute-name, attribute-type>
    // index on each attribute value:
    // SortedMuliSet<{attribute-value, [key, attribute-name]}> Guava Set that can have multiple values for the same hashed attribute-value
    // Non-functional requirements:
    // shard the keys to multiple processing servers using consistent hashing (check my implementation of consistent hashing here: org.haldokan.edge.ConsistentHashing)
    // sharding-copy-servers copy each shard to 3/5 of the servers that serve the queries on the key/val table
    // Master-server keep track of hash range mapping to machines and share replica locations. It receives client requests and based on the key submit the request to the shard
    // server which sends the data back directly to the client w/o the involvement of the master.
}
