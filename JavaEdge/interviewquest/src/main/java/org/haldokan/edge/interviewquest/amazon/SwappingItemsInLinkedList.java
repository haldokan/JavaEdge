package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Lists;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to an Amazon interview question - it handles swapping for all K values including when the first node comes
 * after the 2nd node: for example a list of 4 elements and K = 3;
 * <p>
 * The Question: 4_STAR
 * <p>
 * Swap the elements in Kth position from the start and end of a link list.
 * example:
 * input: list: 1,2,4,5,7,8 & K=2
 * output: 1,7,4,5,2,8
 * <p>
 * Created by haytham.aldokanji on 7/31/16.
 */
public class SwappingItemsInLinkedList<E> {

    public static void main(String[] args) {
        testListLength();
        testFindSwapNodes();
        testFindNodeAt();
        testSwapNodes1();
        testSwapNodes2();
        testSwapNodes3();
        testSwapNodes4();
        testSwapNodes5();
        testSwapNodes6();
    }

    private static void testListLength() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(10);

        int listLen = swapper.getListLength(list);
        assertThat(listLen, is(10));
    }

    private static void testFindSwapNodes() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(10);

        int listLen = swapper.getListLength(list);
        List<Node<Integer>> swapNodes = swapper.findSwapNodesPredecessors(list, 3, listLen);

        assertThat(swapNodes.get(0).value, is(2));
        assertThat(swapNodes.get(1).value, is(7));
    }

    private static void testFindNodeAt() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(10);

        Node<Integer> node = swapper.findNodeAt(list, 6); // 0-based indexing
        assertThat(node.value, is(7));
    }

    private static void testSwapNodes1() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(10);

        Node<Integer> start = swapper.swap(list, 3);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 2).value, is(8));
        assertThat(swapper.findNodeAt(start, 7).value, is(3));
    }

    private static void testSwapNodes2() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(10);

        Node<Integer> start = swapper.swap(list, 1);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 0).value, is(10));
        assertThat(swapper.findNodeAt(start, 9).value, is(1));
    }

    // swapping a node with itself
    private static void testSwapNodes3() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(3);

        Node<Integer> start = swapper.swap(list, 2);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 0).value, is(1));
        assertThat(swapper.findNodeAt(start, 1).value, is(2));
        assertThat(swapper.findNodeAt(start, 2).value, is(3));
    }

    private static void testSwapNodes4() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(4);

        Node<Integer> start = swapper.swap(list, 3);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 0).value, is(1));
        assertThat(swapper.findNodeAt(start, 1).value, is(3));
        assertThat(swapper.findNodeAt(start, 2).value, is(2));
        assertThat(swapper.findNodeAt(start, 3).value, is(4));
    }

    private static void testSwapNodes5() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(3);

        Node<Integer> start = swapper.swap(list, 3);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 0).value, is(3));
        assertThat(swapper.findNodeAt(start, 1).value, is(2));
        assertThat(swapper.findNodeAt(start, 2).value, is(1));
    }

    private static void testSwapNodes6() {
        SwappingItemsInLinkedList<Integer> swapper = new SwappingItemsInLinkedList<>();
        Node<Integer> list = swapper.makeIntegerList(2);

        Node<Integer> start = swapper.swap(list, 2);
        System.out.printf("%s%n", swapper.listToString(start));

        assertThat(swapper.findNodeAt(start, 0).value, is(2));
        assertThat(swapper.findNodeAt(start, 1).value, is(1));
    }

    // we assume that the passed node is the start of the List
    public Node<E> swap(Node<E> node, int k) {
        int ordinal = k;
        if (node == null) {
            throw new NullPointerException("Null list");
        }

        int listLen = getListLength(node);
        if (listLen < ordinal || listLen == 1) {
            return node;
        }
        if (ordinal == listLen) {
            // it is the same as swapping 1 and last element - think of it. this assignment saves us accounting of corner cases
            ordinal = 1;
        }

        Node<E> start = node;
        List<Node<E>> swapNodesPredecessors = findSwapNodesPredecessors(node, ordinal, listLen);
        Node<E> swapNode1Predecessor = swapNodesPredecessors.get(0);
        Node<E> swapNode2Predecessor = swapNodesPredecessors.get(1);

        Node<E> swapNode2 = swapNode2Predecessor.next;
        Node<E> swapNode1 = ordinal == 1 ? swapNode1Predecessor : swapNode1Predecessor.next;

        if (ordinal == 1) {
            start = swapNode2;
        } else {
            swapNode1Predecessor.next = swapNode2;
        }
        swapNode2Predecessor.next = swapNode1;
        Node<E> swapNode1Next = swapNode1.next;
        swapNode1.next = swapNode2.next;
        swapNode2.next = swapNode1Next;

        return start;
    }

    private List<Node<E>> findSwapNodesPredecessors(Node<E> node, int k, int listLen) {
        Node<E> current = node;
        Node<E> swapNode1 = node;
        Node<E> swapNode2 = node;

        int index = 1;
        int found = k == 1 ? 1 : 0;

        while (current != null) {
            if (k - index == 1) {
                swapNode1 = current;
                found++;
            }
            if (index == listLen - k) {
                swapNode2 = current;
                found++;
            }
            // no need to traverse the whole list once we marked the items we want to swap
            if (found >= 2) {
                break;
            }
            index++;
            current = current.next;
        }
        List<Node<E>> swapNodes = Lists.newArrayList();
        swapNodes.add(swapNode1);
        swapNodes.add(swapNode2);

        return swapNodes;
    }

    private int getListLength(Node<E> node) {
        Node<E> current = node;

        int listLen = 0;
        while (current != null) {
            listLen++;
            current = current.next;
        }
        return listLen;
    }

    private Node<E> findNodeAt(Node<E> start, int index) {
        Node<E> current = start;
        int counter = 0;
        // 0-based indexing
        while (counter < index && current != null) {
            current = current.next;
            counter++;
        }
        if (current == null) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return current;
    }

    private String listToString(Node<E> start) {
        StringBuilder builder = new StringBuilder();
        Node<E> current = start;

        while (current != null) {
            builder.append(current).append(", ");
            current = current.next;
        }
        return builder.toString();
    }

    private Node<Integer> makeIntegerList(int numberOfNodes) {
        Node<Integer> current = new Node<>(1);
        Node<Integer> start = current;

        for (int i = 2; i <= numberOfNodes; i++) {
            Node<Integer> node = new Node<>(i);
            current.next = node;
            current = node;
        }
        return start;
    }

    public static final class Node<E> {
        private final E value;
        Node<E> next;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
