package org.haldokan.edge.interviewquest.google;

/**
 * My solution to a Google interview question - unlike my solution in NextLargestNumberInBST, this one does not piggyback
 * on in-order traversal of the tree. It identifies 2 cases:
 * 1- target node has left child(ren): traverse down to the right-most leaf
 * 2- target node is a leaf: take the value of the ancestor that has the closest value
 *
 * Solution runs in log(n)
 *
 * The Question: 5-STAR
 *
 * For a given node in a binary search tree find the next largest number in search tree.
 */
public class NextLargestNumberInBST2 {
    public static void main(String[] args) {
        NextLargestNumberInBST2 driver = new NextLargestNumberInBST2();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
    }

    Float nextLargest(Node node, Node parent, float val) {
        if (node == null) {
            return null;
        }
        if (node.val == val) {
            if (node.left == null) {
//                System.out.printf("return->%s%n", parent);
                return val > parent.val ? parent.val : null; // condition handles the case where the target value is the smallest in the tree
            }
            // next largest is the leaf at the right most
            Node node1 = node.left;
            Node parent1 = null;
            while (node1 != null) {
                parent1 = node1;
                node1 = node1.right;
            }
            return parent1.val;
        }
        if (val < node.val) {
//            System.out.printf("left->node %s, parent %s%n", node, parent);
            return nextLargest(node.left, parent, val); // left does not move the parent - think of it!
        }
//        System.out.printf("right->node %s, parent %s%n", node, parent);
        return nextLargest(node.right, node, val); // right does move the parent because if val > node the next largest is possibly the current node
    }

    void test1() {
        Node root = new Node(10);
        Node n2 = new Node(7);
        Node n3 = new Node(9);
        Node n4 = new Node(8);
        Node n5 = new Node(8.5f);
        Node n6 = new Node(8.6f);
        Node n7 = new Node(12);
        Node n8 = new Node(11);
        Node n9 = new Node(11.5f);
        Node n10 = new Node(11.3f);

        root.left = n2;
        root.right = n7;

        n7.left = n8;
        n8.right = n9;
        n9.left = n10;

        n2.right = n3;
        n3.left = n4;

        n4.right = n5;
        n5.right = n6;

        System.out.println(nextLargest(root, root, 8));
        System.out.println(nextLargest(root, root, 8.5f));
        System.out.println(nextLargest(root, root, 11f));
        System.out.println(nextLargest(root, root, 7));
        System.out.println(nextLargest(root, root, 11.5f));
        System.out.println(nextLargest(root, root, 9));
        System.out.println(nextLargest(root, root, 12));
    }

    void test2() {
        Node root = new Node(12);
        Node n5 = new Node(10f);
        Node n6 = new Node(11.6f);
        Node n7 = new Node(11.9f);
        Node n8 = new Node(11);
        Node n9 = new Node(11.5f);
        Node n10 = new Node(11.7f);

        root.left = n7;
        n7.left = n5;
        n5.right = n8;
        n8.right = n9;
        n9.right = n10;
        n10.left = n6;
        System.out.println(nextLargest(root, root, 11.6f));
    }

    void test3() {
        Node root = new Node(7);
        Node n2 = new Node(12);
        Node n3 = new Node(9);
        Node n4 = new Node(8);
        Node n5 = new Node(10f);
        Node n6 = new Node(9.5f);
        Node n7 = new Node(9.3f);

        root.right = n4;
        n4.right = n3;
        n3.right = n5;
        n5.left = n6;
        n6.left = n7;
        System.out.println(nextLargest(root, root, 9.3f));
    }

    void test4() {
        Node root = new Node(12);
        Node n2 = new Node(7);
        Node n3 = new Node(9);
        Node n4 = new Node(8);
        Node n5 = new Node(10f);
        Node n6 = new Node(9.5f);
        Node n7 = new Node(9.3f);

        root.left = n5;
        n5.left = n3;
        n3.left = n4;
        n4.left = n2;
        System.out.println(nextLargest(root, root, 7f));
    }
    private static class Node {
        private float val;
        private Node left;
        private Node right;

        public Node(float val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}
