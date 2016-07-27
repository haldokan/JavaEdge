package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.ImmutableMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * My solution to an Amazon interview question - used a composite map of  year -> day of the year -> page url -> UserVisit
 * as a data structure.
 *
 * The Question: 4_STAR
 * <p>
 * Entry in the log file is like this:
 * User 1 visited Page 4
 * User 3 visited Page 2
 * User 7 visited Page 9
 * .....................
 * .....................
 * Design an efficient data structure which supports queries like the following:
 * Which pages were visited by a minimum N number of users on a specific day?
 * Which pages were visited by a minimum number of users N a minimum number M of times on a specific day?
 * Which pages were visited by a specific user more than a number N of times on a specific day?
 * <p>
 * Created by haytham.aldokanji on 7/25/16.
 */
public class QueryingLogfileEfficiently {
    // map of: year -> day of the year -> page url -> UserVisit
    private final Map<Integer, Map<Integer, Map<String, UserVisit>>> pageVisitsLog = new ConcurrentHashMap<>();
    private final Map<String, Page> pageByUrl = new HashMap<>();
    private final Map<String, User> userByUsername = new HashMap<>();

    public static void main(String[] args) {
        QueryingLogfileEfficiently driver = new QueryingLogfileEfficiently();
        driver.testLogPageVisit();
    }

    public void logPageVisit(User user, Page page) {
        LocalDateTime dateTime = LocalDateTime.now();
        int year = dateTime.getYear();
        int dayOfYear = dateTime.getDayOfYear();
        PageVisit pageVisit = new PageVisit(user, page, dateTime);

        Map<Integer, Map<String, UserVisit>> visitsForYear =
                pageVisitsLog.computeIfAbsent(year, dayOfYearVisits -> new HashMap<>());

        Map<String, UserVisit> visitsForDayOfYear =
                visitsForYear.computeIfAbsent(dayOfYear, urlVisits -> new HashMap<>());

        UserVisit visitsForUrl =
                visitsForDayOfYear.computeIfAbsent(page.url, userVisits -> new UserVisit());

        visitsForUrl.addVisit(pageVisit);
    }

    public Set<String> pagesVisited(LocalDate date, int minNumberUsers, int minNumberVisits, Set<String> users) {
        int year = date.getYear();
        int dayOfYear = date.getDayOfYear();
        // should check for NP but skipping that for brevity
        Map<String, UserVisit> visitedPagesForDayOfYear = pageVisitsLog.get(year).get(dayOfYear);

        return visitedPagesForDayOfYear.entrySet().stream()
                .filter(userVisits -> userVisits.getValue().getNumberOfUsers() >= minNumberUsers)
                .filter(userVisits -> userVisits.getValue().getTotalNumOfVisits() >= minNumberVisits)
                .filter(userVisits -> userVisits.getValue().getUserNames().containsAll(users))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    // looking at this method, is it hard to conclude that 'nested' functional call is an anti-pattern? It is hard to
    // understand what this long nested pipe does. Perhaps the nested parts should be extracted out
    public Set<String> pagesVisitedByUserMinNumberOfTimes(String username, int minNumOfVisits, int year) {
        Set<String> pages = new HashSet<>();
        pageVisitsLog.get(year).values().stream()
                .forEach(yearVisits -> yearVisits.values().stream()
                        .forEach(userVisits ->
                                userVisits.getUserVisits().entrySet().stream()
                                        .filter(e -> e.getKey().equals(username))
                                        .map(Map.Entry::getValue)
                                        .forEach(listOfVisits -> listOfVisits.stream()
                                                .collect(Collectors.groupingBy(
                                                        visit -> visit.page.url, Collectors.summingInt(visit -> 1)))
                                                .entrySet().stream()
                                                .filter(e -> e.getValue() >= minNumOfVisits)
                                                .map(Map.Entry::getKey)
                                                .forEach(pages::add))));
        return pages;
    }

    private void testLogPageVisit() {
        Random random = new Random();
        int numberOfVisits = 1000;
        for (int i = 0; i < numberOfVisits; i++) {
            logPageVisit(new User("username" + random.nextInt(10)), new Page("www.url" + random.nextInt(20) + ".com"));
        }
        System.out.printf("log: %s%n", pageVisitsLog);

        Set<String> pagesVisited = pagesVisited(LocalDate.now(), 3, 5, Collections.emptySet());
        System.out.printf("pages: %s%n", pagesVisited);

        Set<String> pagesVisitedForUser = pagesVisitedByUserMinNumberOfTimes("username9", 5, 2016);
        System.out.printf("pages4User: %s%n", pagesVisitedForUser);
    }

    private final static class User {
        private final String username;

        public User(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return username;
        }
    }

    private final static class Page {
        private final String url;

        public Page(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return url;
        }
    }

    private final static class PageVisit {
        private final User user;
        private final Page page;
        private final LocalDateTime time;

        public PageVisit(User user, Page page, LocalDateTime time) {
            this.user = user;
            this.page = page;
            this.time = time;
        }

        @Override
        public String toString() {
            return user + "->" + page + "@" + time;
        }
    }

    private final static class UserVisit {
        private final Map<String, List<PageVisit>> userVisits = new HashMap<>();
        private int totalNumOfVisits;

        public void addVisit(PageVisit pageVisit) {
            List<PageVisit> visits = userVisits.computeIfAbsent(pageVisit.user.username, v -> new ArrayList<>());
            visits.add(pageVisit);
            totalNumOfVisits++;
        }

        public int getNumberOfUsers() {
            return userVisits.size();
        }

        public int getTotalNumOfVisits() {
            return totalNumOfVisits;
        }

        public Set<String> getUserNames() {
            return new HashSet<>(userVisits.keySet());
        }

        public Map<String, List<PageVisit>> getUserVisits() {
            // in a real app copying this much data can prove costly. returning the actual map is not a good idea: it
            // can be modified by the client code. Also doing that in a multi-threaded environment is not possible
            return ImmutableMap.copyOf(userVisits);
        }

        @Override
        public String toString() {
            return "UserVisit{" +
                    "userVisits=" + userVisits +
                    ", totalNumOfVisits=" + totalNumOfVisits +
                    '}';
        }
    }
}
