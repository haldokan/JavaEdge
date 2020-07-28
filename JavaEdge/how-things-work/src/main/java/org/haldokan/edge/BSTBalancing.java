package org.haldokan.edge;

/**
 * work in progress
 * Balance a BST
 * 07/27/20
 */
public class BSTBalancing {
    public static void main(String[] args) {
        BSTBalancing driver = new BSTBalancing();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
        driver.test5();
        driver.test6();
    }

    Node rotateRight(Node node) {
        Node left = node.left;
        left.right = node;
        node.left = null;
        return left;
    }

    Node rotateLeft(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = null;
        node.left.left = left;
        return node;
    }

    Node doubleRotate(Node node) {
        return rotateRight(rotateLeft(node));
    }

    Node add(Node tree, Node node) {
        if (tree == null) {
            return node;
        }
        tree.balance++;
        if (node.val < tree.val) {
            tree.left = add(tree.left, node);
        } else {
            tree.right = add(tree.right, node);
        }
        return tree;
    }

    Node find(Node tree, int val) {
        if (tree == null) {
            return null;
        }
        if (tree.val == val) {
            return tree;
        }
        if (val < tree.val) {
            return find(tree.left, val);
        }
        return find(tree.right, val);
    }

    static class Node {
        private int val;
        private Node left;
        private Node right;
        private int balance;

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", val, balance);
        }
    }

    Node makeTree1() {
        Node root = new Node(2);
        root.left = new Node(1);
        root.left.left = new Node(0);
        return root;
    }

    Node makeTree2() {
        Node root = new Node(3);
        root.left = new Node(1);
        root.left.right = new Node(2);
        return root;
    }

    void test1() {
        Node tree = makeTree1();
        System.out.printf("%s, %s, %s%n", tree, tree.left, tree.left.left);
    }

    void test2() {
        Node tree = makeTree1();
        Node rotatedTree = rotateRight(tree);
        System.out.printf("%s, %s, %s%n", rotatedTree, rotatedTree.left, rotatedTree.right);
        System.out.printf("%s, %s, %s%n", tree, tree.left, tree.right);
    }

    void test3() {
        Node tree = makeTree2();
        System.out.printf("%s, %s, %s%n", tree, tree.left, tree.left.right);
        Node rotatedTree = rotateLeft(tree);
        System.out.printf("%s, %s, %s%n", rotatedTree, rotatedTree.left, rotatedTree.left.left);
    }

    void test4() {
        Node tree = makeTree2();
        System.out.printf("%s, %s, %s%n", tree, tree.left, tree.left.right);
        Node rotatedTree = doubleRotate(tree);
        System.out.printf("%s, %s, %s%n", rotatedTree, rotatedTree.left, rotatedTree.right);
    }

    void test5() {
        Node root = new Node(3);
        add(root, new Node(1));
        System.out.printf("%s, %s%n", root, root.left);

        add(root, new Node(2));
        System.out.printf("%s, %s, %s%n", root, root.left, root.left.right);

        add(root, new Node(0));
        System.out.printf("%s, %s, %s %s%n", root, root.left, root.left.left, root.left.right);
    }

    void test6() {
        Node tree = makeTree2();
        System.out.println(find(tree, 2));
        System.out.println(find(tree, 1));
        System.out.println(find(tree, 3));
    }
}
