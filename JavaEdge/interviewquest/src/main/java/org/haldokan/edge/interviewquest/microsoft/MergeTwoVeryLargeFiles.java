package org.haldokan.edge.interviewquest.microsoft;

/**
 * My solution to a Microsoft interview question
 *
 * The Question: 3.5-STAR
 *
 * Given two very large files – first contains Id and name, another one contains Id and address – you need to create 3rd
 * file which will contain id, name, and address. First, ask the clarifying questions and then tell the approach.
 * 11/6/20
 */
public class MergeTwoVeryLargeFiles {
    // description of a solution:

    // >>> case1 <<<: entries in both files are ordered by user id
    // stream both files at the same time and merge the corresponding lines writing each merged line back to the file system (possibly using buffered output stream)

    // >>> case2 <<<: entries have different order b/w the 2 files (use external merge sort)
    // suppose that each of the files has N entries; suppose memory can fit M entries
    // read the id-name file M entries at a time; sort the entries on id and write the sorted entries to the file system creating files m1, m1, ...
    // the previous step creates N/M files on the files system with each having M (possible less for the last file) entries
    // stream the N/M files back into memory one line at a time and merge the sorted entries we can write out the merged entries
    // to the file system every time there is M entries in memory
    // *** Repeat the same steps for the id-address file. Once that done case2 becomes the same as case1
}
