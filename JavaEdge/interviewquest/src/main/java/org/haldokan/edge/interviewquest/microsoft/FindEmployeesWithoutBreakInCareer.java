package org.haldokan.edge.interviewquest.microsoft;

import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * My solution to a Microsoft interview question
 *
 * The Question: 3-STAR
 *
 * Two tables employee (contains Id and name) and employee details having work experience history
 * (contains Id, fromYear, toYear) – find out all the employees who worked w/o career break.
 *
 * Java implementation for the same.
 * From the same table list the name, fromYear, toYear for all employee.
 * Dependency Injection.
 * Design patterns which can be used.
 * 11/5/20
 */
public class FindEmployeesWithoutBreakInCareer {
    public List<Employee> employeesWithContinuousCareer(List<Employee>  employees) {
        List<Employee> employeesWithoutBreak = new ArrayList<>();

        for (Employee employee : employees) {
            List<Employment> employments = employee.getEmployments();

            LocalDate endDate = null;
            boolean breaks = false;
            // this can be extracted to a method
            for (Employment employment : employments) {
                LocalDate employmentStartDate = employment.getStartDate();
                if (endDate != null && !employmentStartDate.isEqual(endDate)) {
                    breaks = true;
                    break;
                }
                endDate = employmentStartDate;
            }
            if (!breaks) {
                employeesWithoutBreak.add(employee);
            }
        }
        return employeesWithoutBreak;
    }

    private static final class Employee {
        private final String id;
        private final String name;
        private final List<Employment> employments;

        // we can assume that employments are sorted on startDate using ORDER BY (start_date) in the db query that gets the employments
        public Employee(String id, String name, List<Employment> employments) {
            this.id = id;
            this.name = name;
            this.employments = ImmutableList.<Employment>builder().addAll(employments).build();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Employment> getEmployments() {
            return employments;
        }
    }

    private static final class Employment {
        private final String company;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public Employment(String company, LocalDate startDate, LocalDate endDate) {
            this.company = company;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getCompany() {
            return company;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }
}
