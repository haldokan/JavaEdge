package org.haldokan.edge.interviewquest.facebook;

import java.util.IdentityHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question - used IdentityHashMap to keep track of random pointers
 * The Question: 4_STAR
 * Given a linked list where apart from the next pointer, every node also has a pointer named random which can point
 * to any other node in the linked list. Make a copy of the linked list.
 * <p>
 * Created by haytham.aldokanji on 5/8/16.
 */
public class CopyLinkedListWithRandPointers {

    public static void main(String[] args) {
        CopyLinkedListWithRandPointers driver = new CopyLinkedListWithRandPointers();

        Node original = driver.makeList();
        Node copy = driver.copy(original);
        driver.print(copy);
        driver.assertCopySameAsOriginal(original, copy);
    }

    // we should assert the structure of the 2 lists is the same but I'm not going to do that here
    private void assertCopySameAsOriginal(Node original, Node copy) {
        if (original == null) {
            return;
        }
        assertThat(original.getVal(), is(copy.getVal()));
        Node random = original.getRandom();
        if (random != null) {
            assertThat(original.getRandom().getVal(), is(copy.getRandom().getVal()));
        }
        // recurse
        assertCopySameAsOriginal(original.next, copy.next);
    }

    public Node copy(Node originalList) {
        if (originalList == null) {
            return null;
        }
        // to avoid re-assigning the passed ref (bad practice)
        Node original = originalList;
        Map<Node, Node> nodeTracker = new IdentityHashMap<>();
        Node copyTail = new Node(original.getVal());
        Node copyHead = copyTail;

        nodeTracker.put(original, copyTail);
        setRandom(nodeTracker, original, copyTail);

        original = original.getNext();
        while (original != null) {
            copyTail.setNext(original);
            copyTail = copyTail.getNext();
            nodeTracker.put(original, copyTail);
            setRandom(nodeTracker, original, copyTail);

            original = original.getNext();
        }
        return copyHead;
    }

    private void setRandom(Map<Node, Node> nodeTracker, Node original, Node copy) {
        Node random = original.getRandom();
        if (random != null) {
            Node randomCopy = nodeTracker.computeIfAbsent(random, v -> new Node(random.getVal()));
            copy.setRandom(randomCopy);
        }
    }

    private void print(Node list) {
        while (list != null) {
            System.out.println(list);
            list = list.getNext();
        }
    }

    private Node makeList() {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);

        n1.setNext(n2);
        n1.setRandom(n5);
        n2.setNext(n3);
        n2.setRandom(n1);
        n3.setNext(n4);
        n3.setRandom(n1);
        n4.setNext(n5);
        n4.setRandom(n4);
        n5.setRandom(n2);

        return n1;
    }

    private static class Node {
        private int val;
        private Node next;
        private Node random;

        public Node(int val) {
            this.val = val;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public int getVal() {
            return val;
        }

        public Node getRandom() {
            return random;
        }

        public void setRandom(Node random) {
            this.random = random;
        }

        @Override
        public String toString() {
            String randVal = random != null ? String.valueOf(random.val) : "";
            return String.valueOf(val + "/" + randVal);
        }
    }
}
