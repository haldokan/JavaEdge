package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question
 * <p>
 * Write a program that reverses a linked list without using more than O(1) storage.
 * Created by haytham.aldokanji on 5/12/16.
 */
public class ReverseLinkedListInSitu {
    public static void main(String[] args) {
        ReverseLinkedListInSitu driver = new ReverseLinkedListInSitu();

        driver.testGetTail();
        driver.testReverse();
    }

    public Node reverse(Node list) {
        Node head = list;
        Node tail = getTail(list);

        while (head != tail) {
            Node tmpTail = tail.next;
            Node tmpHead = head;
            tail.next = tmpHead;
            head = head.next;
            tmpHead.next = tmpTail;
        }
        return head;
    }

    public Node getTail(Node list) {
        Node refCopy = list;
        Node tail = null;

        while (refCopy != null) {
            tail = refCopy;
            refCopy = refCopy.next;
        }
        return tail;
    }

    private Integer[] toArray(Node node) {
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.getVal());
            node = node.next;
        }
        return list.toArray(new Integer[list.size()]);
    }

    private Node makeList() {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);

        n1.setNext(n2);
        n2.setNext(n3);
        n3.setNext(n4);
        n4.setNext(n5);

        return n1;
    }

    private void testGetTail() {
        Node list = makeList();
        Node tail = getTail(list);
        assertThat(tail.getVal(), is(5));
    }

    private void testReverse() {
        Node listOriginal = makeList();
        Node workList = makeList();
        Node reversed = reverse(workList);

        Integer[] originalArr = toArray(listOriginal);
        System.out.println(Arrays.toString(originalArr));
        Integer[] reversedArr = toArray(reversed);
        System.out.println(Arrays.toString(reversedArr));

        assertThat(originalArr.length, is(reversedArr.length));
        for (int i = 0; i < originalArr.length; i++) {
            assertThat(originalArr[i], is(reversedArr[reversedArr.length - i - 1]));
        }
    }

    private static class Node {
        private int val;
        private Node next;

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

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}
