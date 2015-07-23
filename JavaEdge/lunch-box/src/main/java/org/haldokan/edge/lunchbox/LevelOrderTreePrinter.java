package org.haldokan.edge.lunchbox;

import java.util.LinkedList;
import java.util.Queue;


/**
* My solution to a Linkedin interview question found on Careercup
*  
* Print a tree in Level Order with a newline after each depth
* 
* Sample input:
*
*          1
*         / \
*        3   5
*       / \   \
*      2   4   7
*     /     \
*    9       8
*
* Expected output:
*    1
*    3 5
*    2 4 7
*    9 8
*    ==========
*/
public class LevelOrderTreePrinter {
    
    public static void main(String[] args) {
	Tree t = new Tree(1);
	Tree n3 = new Tree(3);
	Tree n5 = new Tree(5);
	t.l = n3;
	t.r = n5;
	
	Tree n2 = new Tree(2);
	Tree n4 = new Tree(4);
	n3.l = n2;
	n3.r = n4;
	
	Tree n9 = new Tree(9);
	Tree n8 = new Tree(8);
	Tree n7 = new Tree(7);
	n2.l = n9;
	n4.r = n8;
	n5.r = n7;
	
	new LevelOrderTreePrinter().levelOrderPrint(t);
    }
    
    private void levelOrderPrint(Tree t) {
	Queue<Tree> q = new LinkedList<>();
	Queue<Integer> levelqu = new LinkedList<>();
	
	System.out.println(t);
	addNodeChildren(q, t, levelqu, 1);
	int level = 1;
	while (!q.isEmpty()) {
	    Tree n = q.poll();
	    int nodeLevel = levelqu.poll();
	    if (nodeLevel > level) {
		System.out.println();
		level = nodeLevel;
	    }
	    System.out.print(n);
	    addNodeChildren(q, n, levelqu, nodeLevel + 1);
	}
	
    }
    
    private void addNodeChildren(Queue<Tree> q, Tree n, Queue<Integer> levelqu, int level) {
	if (n.l != null) {
	    q.add(n.l);
	    levelqu.add(level);
	}
	if (n.r != null) {
	    q.add(n.r);
	    levelqu.add(level);
	}
    }
    
    private static class Tree {
	private int val;
	private Tree l, r;
	
	public Tree(int val) {
	    this.val = val;
	}

	@Override
	public String toString() {
	    return "(" + val + ")";
	}
    }
}
