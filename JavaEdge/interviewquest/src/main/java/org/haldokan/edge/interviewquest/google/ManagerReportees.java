package org.haldokan.edge.interviewquest.google;

/**
 * My proposed solution to a Google interview question
 * <p>
 * There is a company with 250 employees. Its records contain: EmployeeID, ManagerID (which is a reference to the EmployeeID of the manager).
 * <p>
 * Part 1. List directly reporting employees for a given ID
 * <p>
 * Part 2. List all (also indirectly) reporting employees to a given ID
 * Created by haytham.aldokanji on 10/6/15.
 */
public class ManagerReportees {
    // This is a relatively simple question if one knows about BFS of n-ary trees
    // We construct n-ary tree of Employee {empId, List<Employee>}
    // While constructing the tree we keep pointers to each employee in a hashset so we can find the manager in O(1) time
    // We do a BFS on the n-ary tree starting at the manager id node using a queue to manage the traversal
    // a variation on  BFS is shown in this repository under linkedin/LevelOrderTreePrinter.java
}
