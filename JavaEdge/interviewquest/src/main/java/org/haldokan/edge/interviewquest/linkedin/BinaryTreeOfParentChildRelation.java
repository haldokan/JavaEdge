package org.haldokan.edge.interviewquest.linkedin;

import org.junit.Assert;

import java.util.*;

/**
 * My solution to a Linkedin interview question. I believe there is no nice recursive solution for this problem
 * <p>
 * Given a list of child->parent relationships, build a binary tree out of it. All the element Ids inside the tree are unique.
 * <p>
 * Example:
 * <p>
 * Given the following relationships:
 * <p>
 * Child Parent IsLeft
 * 15 20 true
 * 19 80 true
 * 17 20 false
 * 16 80 false
 * 80 50 false
 * 50 null false
 * 20 50 true
 * 7 19 true
 * 31 19 false
 * <p>
 * You should return the following tree:
 * 50
 * /   \
 * 20    80
 * / \    / \
 * 15 17 19 16
 * /\
 * 7  31
 *
 * @author haldokan
 */
public class BinaryTreeOfParentChildRelation {

    public static void main(String[] args) {
        Relation r1 = new Relation(15, 20, true);
        Relation r2 = new Relation(19, 80, true);
        Relation r3 = new Relation(17, 20, false);
        Relation r4 = new Relation(16, 80, false);
        Relation r5 = new Relation(80, 50, false);
        Relation r6 = new Relation(50, null, false);
        Relation r7 = new Relation(20, 50, true);
        Relation r8 = new Relation(7, 19, true);
        Relation r9 = new Relation(31, 19, false);

        BinaryTreeOfParentChildRelation treeBuilder = new BinaryTreeOfParentChildRelation();
        List<Relation> rels = Arrays.asList(new Relation[]{r1, r2, r3, r4, r5, r6, r7, r8, r9});
        List<Integer> nodeIds = new ArrayList<>();
        treeBuilder.walkTree(treeBuilder.buildTree(rels), nodeIds);

        for (int i = 0; i < 5; i++) {
            Collections.shuffle(rels);
            List<Integer> ids = new ArrayList<>();
            treeBuilder.walkTree(treeBuilder.buildTree(rels), ids);
            System.out.println(ids);
            Assert.assertArrayEquals(nodeIds.toArray(), ids.toArray());
        }
    }

    private void walkTree(Node<Integer> n, List<Integer> nodeIds) {
        if (n == null)
            return;
        nodeIds.add(n.id);
        walkTree(n.left, nodeIds);
        walkTree(n.right, nodeIds);
    }

    /**
     * Implement a method to build a tree from a list of parent-child relationships And return the root Node of the tree
     */
    private Node<Integer> buildTree(List<Relation> rels) {
        Map<Integer, Node<Integer>> nodeById = new HashMap<>();
        Node<Integer> root = null;
        for (Relation r : rels) {
            if (r.parent == null) {
                nodeById.computeIfAbsent(r.child, k -> new Node<Integer>(r.child));
                root = nodeById.get(r.child);
            } else {
                nodeById.computeIfAbsent(r.parent, k -> new Node<Integer>(r.parent));
                if (r.child != null) {
                    nodeById.computeIfAbsent(r.child, k -> new Node<Integer>(r.child));
                    Node<Integer> parent = nodeById.get(r.parent);
                    Node<Integer> child = nodeById.get(r.child);
                    if (r.isLeft)
                        parent.left = child;
                    else
                        parent.right = child;
                }
            }
        }
        return root;
    }

    /**
     * Represents a pair relation between one parent node and one child node inside a binary tree If the parent is null,
     * it represents the ROOT node
     */
    private static class Relation {
        private Integer parent;
        private Integer child;
        private boolean isLeft;

        public Relation(Integer child, Integer parent, boolean isLeft) {
            this.child = child;
            this.parent = parent;
            this.isLeft = isLeft;
        }

        @Override
        public String toString() {
            return child + "/" + parent + "/" + isLeft;
        }
    }

    /**
     * Represents a single Node inside a binary tree
     */
    private static class Node<E> {
        private E id;
        private Node<E> left;
        private Node<E> right;

        public Node(E id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (id == null) {
                if (other.id != null)
                    return false;
            } else if (!id.equals(other.id))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
