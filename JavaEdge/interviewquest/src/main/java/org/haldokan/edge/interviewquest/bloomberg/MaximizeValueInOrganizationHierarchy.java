package org.haldokan.edge.interviewquest.bloomberg;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @InProgress You are given an organization hierarchy tree (n-ary tree). Every employee (node) has some value (can be -ve or +ve).
 * You have to host a party and have to invite employees such that the total value (summation of each node value) of all
 * the employees is maximum.
 * there is a rule: no one likes to see their bosses in the party. So you cant invite an employee's immediate boss and
 * subordinates at the same time. You can skip more than 1 level if it gives you maximum value.
 * <p>
 * Created by haytham.aldokanji on 8/6/16.
 */
public class MaximizeValueInOrganizationHierarchy {

    public static void main(String[] args) {
        MaximizeValueInOrganizationHierarchy driver = new MaximizeValueInOrganizationHierarchy();
        driver.test();
    }

    public int maximize(Employee employee) {
        Deque<Employee> evalDeck = new ArrayDeque<>();
        Set<Employee> invited = new HashSet<>();

        evalDeck.add(employee);
        int maxValue = employee.getValue();
        invited.add(employee);

        while (!evalDeck.isEmpty()) {
            Employee boss = evalDeck.remove();
            List<Employee> subordinates = boss.getSubordinates();
            subordinates.stream().forEach(evalDeck::add);

            subordinates = subordinates.stream().sorted((e1, e2) ->
                    e2.getValue() - e1.getValue()).collect(Collectors.toList());

            int subordinateValues = 0;
            if (!subordinates.isEmpty()) {
                subordinateValues = subordinates.get(0).getValue();
                subordinates.remove(0);
            }

            List<Employee> valuedSubordinates = new ArrayList<>();
            for (Employee subordinate : subordinates) {
                int subordinateValue = subordinate.getValue();
                int currentValue = subordinateValues + subordinateValue;

                if (currentValue > subordinateValues) {
                    subordinateValues = currentValue;
                    valuedSubordinates.add(subordinate);
                } else {
                    break;
                }
            }

            if (!valuedSubordinates.isEmpty()) {
                if (invited.contains(boss) && subordinateValues > boss.getValue()) {
                    invited.remove(boss);
                    maxValue = maxValue - boss.getValue() + subordinateValues;
                    valuedSubordinates.stream().forEach(invited::add);
                } else if (subordinateValues > 0) {
                    maxValue += subordinateValues;
                    valuedSubordinates.stream().forEach(invited::add);
                }
            }
        }
        System.out.printf("invited: %s%n", invited);
        return maxValue;
    }

    private void test() {
        Employee hierarchy = makeEmployeeHierarchy();
        int maxValue = maximize(hierarchy);
        System.out.printf("%d%n", maxValue);

        assertThat(1, is(1));
    }

    private Employee makeEmployeeHierarchy() {
        Employee f = new Employee("F", 7);
        Employee b = new Employee("B", -1);
        Employee a = new Employee("A", 13);
        Employee d = new Employee("D", -3);
        Employee c = new Employee("C", 17);
        Employee e = new Employee("E", -8);
        Employee g = new Employee("G", 5);
        Employee i = new Employee("I", -11);
        Employee h = new Employee("H", 19);

        f.add(b);
        f.add(g);
        b.add(a);
        b.add(d);
        d.add(c);
        d.add(e);
        g.add(i);
        i.add(h);

        return f;
    }

    private static class Employee {
        private final String id;
        private final int value;
        private final List<Employee> subordinates;

        public Employee(String id, int value) {
            this.id = id;
            this.value = value;
            this.subordinates = new ArrayList<>();
        }

        public void add(Employee subordinate) {
            subordinates.add(subordinate);
        }

        public String getId() {
            return id;
        }

        public int getValue() {
            return value;
        }

        public List<Employee> getSubordinates() {
            return subordinates;
        }

        @Override
        public String toString() {
            return id + "@" + value;
        }
    }

}