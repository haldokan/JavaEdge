package org.haldokan.edge.interviewquest.google;

import java.util.ArrayList;
import java.util.List;

/**
 * Given two integer arrays. Find the longest common subsequence.
 * eg: a =[1 5 2 6 3 7], b = [5 6 7 1 2 3]. return [1 2 3] or [5 6 7]
 */
public class LongestSubSequenceIn2Arrays {
    public static void main(String[] args) {
        LongestSubSequenceIn2Arrays driver = new LongestSubSequenceIn2Arrays();
        driver.test();
    }
    public void test(){
        int[] a = new int[]{1,5,2,6,3,7};
        int[] b = new int[]{5,6,7,1,2,3,5};

        longestSeq(a,b);

        System.out.println();


        int[] c = new int[]{1,5,2,6,3,4,7};
        int[] d = new int[]{5,6,7,1,2,3,5,4};

        longestSeq(c,d);
    }

    class Node{
        int index;
        int data;
        List<Node> kids;

        Node(int index, int data){
            this.index = index;
            this.data = data;
            kids = new ArrayList<>();
        }
    }

    private void longestSeq(int[] a, int[] b) {
        Node root = seq(a, b);
        printLongestPath(root,  maxPath(root, 1), new ArrayList<>());

        System.out.println();

        root = seq(b,a);
        printLongestPath(root,  maxPath(root, 1), new ArrayList<>());
    }

    private int maxPath(Node n, int length){
        if(n==null) return length;
        int max = length;
        for (Node kid: n.kids) {
            int next = maxPath(kid, length+1);
            if(next>max){
                max = next;
            }
        }
        return max;
    }

    private void printLongestPath(Node n, int max, List<Node> list){
        if(n==null) return;
        list.add(n);
        if(list.size()>=max){
            list.stream().filter(node-> node.data!=-100).forEach(node -> System.out.print(node.data+" "));
            System.out.println();
        }
        for (Node kid: n.kids) {
            printLongestPath(kid, max, list);
        }
        list.remove(n);
    }

    private Node seq(int[] a, int[] b) {
        Node root = new Node(-100, -100);
        for (int i = 0; i < a.length; i++) {
            int j = find( a[i], b);
            insert2Tree( root , j, a[i]);
        }
        return root;
    }

    private void insert2Tree(Node next, int index, int data) {
        if(next.index < index) {
            for (Node kid : next.kids) {
                if(kid.index < index){
                    insert2Tree(kid, index, data);
                    return;
                }
            }
        }
        next.kids.add(new Node(index, data));

    }

    private int find(int element, int[] array) {
        for (int i = 0 ; i < array.length; i++) {
            if(element == array[i]){
                return i;
            }
        }
        return -1;
    }
}
