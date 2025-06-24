package unionfind;

import java.util.*;

public class UnionFindAI {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();

    public void union(String p1, String p2) {
        add(p1);
        add(p2);

        String root1 = find(p1);
        String root2 = find(p2);
        System.out.println("root1 root2: " + root1 + " " + root2);
        if (root1.equals(root2)) return; // already in same set

        // Union by rank
        int rank1 = rank.get(root1);
        int rank2 = rank.get(root2);

        if (rank1 < rank2) {
            parent.put(root1, root2);
        } else if (rank1 > rank2) {
            parent.put(root2, root1);
        } else {
            parent.put(root2, root1);
            rank.put(root1, rank1 + 1);
        }
        System.out.println(">>parent: " + parent);
    }

    private String find(String person) {
        if (!parent.get(person).equals(person)) {
            System.out.println("find-person: " + person);
            System.out.println("find-parent: " + parent);
            parent.put(person, find(parent.get(person))); // Path compression
        }
        System.out.println("person: " + person);
        System.out.println("parent: " + parent);
        return parent.get(person);
    }

    public int countGroups() {
        Set<String> roots = new HashSet<>();
        for (String person : parent.keySet()) {
            roots.add(find(person));
        }
        return roots.size();
    }

    public boolean inSameGroup(String p1, String p2) {
        if (!parent.containsKey(p1) || !parent.containsKey(p2)) return false;
        return find(p1).equals(find(p2));
    }

    private void add(String person) {
        if (!parent.containsKey(person)) {
            System.out.println("add-person: " + person);
            parent.put(person, person);
            rank.put(person, 0); // Initial rank is 0
        }
    }
    // The 1st group
    // Alice
    // ├─ Bob
    // └─ Charlie
    //      └─ Daisy (after full path compression (a call to find(Daisy) it looks like below:
    // Alice
    // ├─ Bob
    // ├─ Charlie
    // └─ Daisy
    // The 2nd group
    // Eve
    // └─ Frank
    public static void main(String[] args) {
        UnionFindAI uf = new UnionFindAI();
        String[][] friendships = {
                {"Alice", "Bob"},
                {"Charlie", "Daisy"},
                {"Bob", "Charlie"},
                {"Eve", "Frank"}
        };

        for (String[] pair : friendships) {
            uf.union(pair[0], pair[1]);
        }

        System.out.println("Total friend groups: " + uf.countGroups()); // Expected: 2
        System.out.println("Are Alice and Daisy in the same group? " + uf.inSameGroup("Alice", "Daisy")); // true
        System.out.println("Are Alice and Eve in the same group? " + uf.inSameGroup("Alice", "Eve"));     // false
    }
}
