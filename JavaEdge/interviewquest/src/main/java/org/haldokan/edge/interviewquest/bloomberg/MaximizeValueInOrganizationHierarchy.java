package org.haldokan.edge.interviewquest.bloomberg;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Bloomberg interview question (still needs tidying up and adding tests/asserts)
 *
 * The Question: 3_STAR + 1/2
 *
 * You are given an organization hierarchy tree (n-ary tree). Every employee (node) has some value (can be -ve or +ve).
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

    // todo: break up this little monster -
    public Set<Employee> maximize(Employee employee) {
        Deque<Employee> evalDeck = new ArrayDeque<>();
        Set<Employee> invited = new LinkedHashSet<>();

        Integer maxNegativeValue = null;
        Integer maxValue = null;

        evalDeck.add(employee);
        if (employee.getValue() >= 0) {
            maxValue = employee.getValue();
            invited.add(employee);
        } else {
            maxNegativeValue = employee.getValue();
        }

        while (!evalDeck.isEmpty()) {
            Employee boss = evalDeck.remove();
            List<Employee> subordinates = boss.getSubordinates();
            subordinates.stream().forEach(evalDeck::add);

            int subordinateValues = 0;
            List<Employee> valuedSubordinates = new ArrayList<>();
            for (Employee subordinate : subordinates) {
                int subordinateValue = subordinate.getValue();

                if (subordinateValue > 0) {
                    subordinateValues += subordinateValue;
                    valuedSubordinates.add(subordinate);
                } else if (subordinateValue < 0 && maxNegativeValue == null ||
                        subordinateValue < 0 && subordinateValue > maxNegativeValue) {
                    maxNegativeValue = subordinateValue;
                }

            }

            if (!valuedSubordinates.isEmpty()) {
                if (invited.contains(boss)) {
                    if (subordinateValues > boss.getValue()) {
                        invited.remove(boss);
                        maxValue = maxValue - boss.getValue() + subordinateValues;
                        valuedSubordinates.stream().forEach(invited::add);
                    }
                } else if (subordinateValues > 0) {
                    maxValue += subordinateValues;
                    valuedSubordinates.stream().forEach(invited::add);
                }
            }
        }
        return invited;
    }

    // todo add more tests
    private void test() {
        Employee hierarchy = makeEmployeeHierarchy();
        Set<Employee> invited = maximize(hierarchy);
        System.out.printf("%s%n", invited);
        System.out.printf("%d%n", invited.stream().collect(Collectors.summingInt(Employee::getValue)));
        // todo add asserts
        assertThat(1, is(1));
    }

    private Employee makeEmployeeHierarchy() {
        /**
         *              f(7)
         *          b(-1)     g(5)
         *     a(13)    d(-3)      i(-11)
         *           c(17) e(-8)         h(19)
         */
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