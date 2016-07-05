package org.haldokan.edge.interviewquest.google;

/**
 * My design of a Google interview question
 * The Question: 4_STAR
 * <p>
 * How do you sort a billion rows of data of integers (a few gigabytes) in a file with only 1024KB of main memory.
 * <p>
 * Created by haytham.aldokanji on 7/5/16.
 */
public class ExternalSortWithLimitedMemory {
    // We have 1024 KB memory
    // We have 1 billion rows of integers: 1000_000_000 * 32 / 1000 = 32 million KB
    // We calculate that the max number of integers we can load to memory: 1024 * 1000/32 = 32000
    // We calculate that we need this number of loads: 1 billion rows / 32000 = 31250 loads
    // We sort the file using external merge sort:
    // 1. Read 32000-row chunks from the file, sort them (quick sort) and write them to the file system. We can use multiple
    //    machines (each with 1024KB memory) to do this step in parallel. At the end of this step we will have 31250 files
    //    of sorted integers on the file system with each having 32000 integers
    // 2. Now we have to merge these chunks/files. We can do 2- or 3-way merge but since memory size is quite limited we
    //    do 2 way merge as follow:
    //    a. we can divide the available memory (32000 integers) into 3 regions: (R1) 10000 integers from one file,
    //       (R2) 10000 from another file, and (R3) 12000 integer for merged result.
    //    b. read 10000 integer from the first chunk/file, read 10000 integer from the 2nd chunk/file and merge sort them
    //       to R3 reading from disk every time one of these 3 events happen: R1 or R2 empties, or R3 fills.
    //    c. repeat step b for every 2 files: f1,f2, f3,f4, f5,f6,..............f31249, f31250
    //    d. by the end of step c we will have 31250/2 = 15624 sorted files.
    //    e. we repeat steps from b to d until we have 1 sorted file left. We need log2(31250) passes. 31250 is not power
    //       of 2 so we will have a dangling file at the end that does not have a matching file so we merge it to the
    //       last merged file.
    // In realistic scenarios we have much more memory and n-way merge sort is used as demonstrated here in my solution
    // to another Google interview question: org.haldokan.edge.interviewquest.google.GenerifiedNWaySortedArrayMerge
}
