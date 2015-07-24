package org.haldokan.edge.lunchbox;

import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 *  My solution to a Linkedin interview question

Given a list of child->parent relationships, build a binary tree out of it. All the element Ids inside the tree are unique.

Example:

Given the following relationships:

Child Parent IsLeft
15 20 true
19 80 true
17 20 false
16 80 false
80 50 false
50 null false
20 50 true


You should return the following tree:
50
/ \
20 80
/ \ / \
15 17 19 16

 * @author haldokan
 *
 */
public class BinaryTreeOfParentChildRelation {
    /**
     * Implement a method to build a tree from a list of parent-child relationships And return the root Node of the tree
     */
    public Node buildTree(List<Relation> data) {
	Node tree = null;
	sortRelations(data);
	Deque<Relation> deck = new LinkedList<>();
	
	for (Relation r : data) {
	    Relation r1 = deck.peekFirst();
	    if (r1 == null) {
		deck.push(r);
	    } else if (r.parent.equals(r1.parent)) {
		deck.pop();
		insertInTree(r, r1, tree);
	    }
	    
	}
	return tree;
    }
    
    private void insertInTree(Relation r1, Relation r2, Node tree) {
	
    }
    
    private void sortRelations(List<Relation> data) {
	Collections.sort(data, new Comparator<Relation>(){
	    @Override
	    public int compare(Relation o1, Relation o2) {
		if (o1.parent == null)
		    return 1;
		return o1.parent.compareTo(o2.parent);
	    }});
    }
    
    /**
     * Represents a pair relation between one parent node and one child node inside a binary tree If the parent is
     * null, it represents the ROOT node
     */
    private static class Relation {
	private Integer parent;
	private Integer child;
	private boolean isLeft;
    }

    /**
     * Represents a single Node inside a binary tree
     */
    private static class Node {
	private Integer id;
	private Node left;
	private Node right;

	@Override
	public String toString() {
	    return String.valueOf(id);
	}
    }
}
