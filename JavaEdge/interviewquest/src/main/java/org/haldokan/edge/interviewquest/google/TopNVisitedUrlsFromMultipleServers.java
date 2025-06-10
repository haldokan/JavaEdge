package org.haldokan.edge.interviewquest.google;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Google inteview question - Each server calculates its top N and send to a 'reduce' server that
 * computes the allover top N.
 * The Question: 3_STAR
 * <p>
 * Given a large network of computers, each keeping log files of visited urls, find the top ten of the most visited urls.
 * (i.e. have many large <string (url) -> int (visits)> maps, calculate implicitly <string (url) -> int (sum of visits
 * among all distributed maps), and get the top ten in the combined map)
 * <p>
 * The result list must be exact, and the maps are too large to transmit over the network (especially sending all of
 * them to a central server or using MapReduce directly, is not allowed)
 * <p>
 * Created by haytham.aldokanji on 6/29/16.
 */
public class TopNVisitedUrlsFromMultipleServers {

    public static void main(String[] args) {
        TopNVisitedUrlsFromMultipleServers driver = new TopNVisitedUrlsFromMultipleServers();

        driver.testTopUrl();
        driver.testTopUrlsFromAllServers();
        driver.testTopUrls();
    }

    public List<UrlVisit> topUrls(List<Map<String, Integer>> topUrlsFromEachServer, int top) {
        List<Set<UrlVisit>> topVisits = new ArrayList<>();
        for (Map<String, Integer> topUrlsForServer : topUrlsFromEachServer) {
            topVisits.add(topUrlsFromOneServer(topUrlsForServer, top));
        }

        return topUrlsFromAllServers(topVisits, top);
    }

    private Set<UrlVisit> topUrlsFromOneServer(Map<String, Integer> visitsByUrl, int top) {
        SortedSet<UrlVisit> topUrls = new TreeSet<>((v1, v2) -> v2.visits - v1.visits);

        for (Map.Entry<String, Integer> entry : visitsByUrl.entrySet()) {
            String url = entry.getKey();
            int visits = entry.getValue();

            if (topUrls.size() < top) {
                topUrls.add(new UrlVisit(url, visits));
            } else {
                UrlVisit lowest = topUrls.last();
                if (visits > lowest.visits) {
                    topUrls.remove(lowest);
                    topUrls.add(new UrlVisit(url, visits));
                }
            }
        }
        return topUrls;
    }

    private List<UrlVisit> topUrlsFromAllServers(List<Set<UrlVisit>> visits, int top) {
        return visits.stream()
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(UrlVisit::getUrl, Collectors.summingInt(UrlVisit::getVisits)))
                .entrySet()
                .stream()
                .sorted(((e1, e2) -> e2.getValue() - e1.getValue()))
                .map(e -> new UrlVisit(e.getKey(), e.getValue()))
                .collect(Collectors.toList())
                .stream()
                .limit(top)
                .collect(Collectors.toList());
    }

    private void testTopUrl() {
        Map<String, Integer> visitsByUrl = new HashMap<>();
        visitsByUrl.put("url1", 10);
        visitsByUrl.put("url2", 5);
        visitsByUrl.put("url3", 12);
        visitsByUrl.put("url4", 3);
        visitsByUrl.put("url5", 7);
        visitsByUrl.put("url6", 9);

        Set<UrlVisit> urlVisits = topUrlsFromOneServer(visitsByUrl, 3);
        System.out.printf("%s%n", urlVisits);
        assertThat(urlVisits.stream().map(UrlVisit::getUrl).toArray(), is(new String[]{"url3", "url1", "url6"}));
    }

    private void testTopUrlsFromAllServers() {
        UrlVisit visit1 = new UrlVisit("url1", 10);
        UrlVisit visit2 = new UrlVisit("url1", 15);
        UrlVisit visit3 = new UrlVisit("url2", 5);
        UrlVisit visit4 = new UrlVisit("url2", 17);
        UrlVisit visit5 = new UrlVisit("url3", 12);
        UrlVisit visit6 = new UrlVisit("url3", 7);
        UrlVisit visit7 = new UrlVisit("url4", 20);

        Set<UrlVisit> visitSet1 = new TreeSet<>((v1, v2) -> v2.visits - v1.visits);
        visitSet1.add(visit1);
        visitSet1.add(visit2);

        Set<UrlVisit> visitSet2 = new TreeSet<>((v1, v2) -> v2.visits - v1.visits);
        visitSet2.add(visit3);
        visitSet2.add(visit4);

        Set<UrlVisit> visitSet3 = new TreeSet<>((v1, v2) -> v2.visits - v1.visits);
        visitSet3.add(visit5);
        visitSet3.add(visit6);

        Set<UrlVisit> visitSet4 = new TreeSet<>((v1, v2) -> v2.visits - v1.visits);
        visitSet4.add(visit7);

        List<Set<UrlVisit>> visits = new ArrayList<>();
        visits.add(visitSet1);
        visits.add(visitSet2);
        visits.add(visitSet3);
        visits.add(visitSet4);

        List<UrlVisit> topVisits = topUrlsFromAllServers(visits, 3);
        System.out.printf("%s%n", topVisits);

        assertThat(topVisits.stream().map(UrlVisit::toString).toArray(),
                is(new String[]{"url1(25)", "url2(22)", "url4(20)"}));
    }

    private void testTopUrls() {
        UrlVisit visit1 = new UrlVisit("url1", 10);
        UrlVisit visit2 = new UrlVisit("url1", 15);
        UrlVisit visit3 = new UrlVisit("url2", 5);
        UrlVisit visit4 = new UrlVisit("url2", 17);
        UrlVisit visit5 = new UrlVisit("url3", 12);
        UrlVisit visit6 = new UrlVisit("url3", 7);
        UrlVisit visit7 = new UrlVisit("url4", 20);

        Map<String, Integer> visitsByUrl1 = new HashMap<>();
        visitsByUrl1.put(visit1.getUrl(), visit1.getVisits());
        visitsByUrl1.put(visit3.getUrl(), visit3.getVisits());

        Map<String, Integer> visitsByUrl2 = new HashMap<>();
        visitsByUrl2.put(visit2.getUrl(), visit2.getVisits());
        visitsByUrl2.put(visit6.getUrl(), visit6.getVisits());

        Map<String, Integer> visitsByUrl3 = new HashMap<>();
        visitsByUrl3.put(visit5.getUrl(), visit5.getVisits());
        visitsByUrl3.put(visit4.getUrl(), visit4.getVisits());

        Map<String, Integer> visitsByUrl4 = new HashMap<>();
        visitsByUrl4.put(visit7.getUrl(), visit7.getVisits());

        List<Map<String, Integer>> visits = Lists.newArrayList(visitsByUrl1, visitsByUrl2, visitsByUrl3, visitsByUrl4);

        List<UrlVisit> topVisits = topUrls(visits, 3);
        System.out.printf("%s%n", topVisits);

        assertThat(topVisits.stream().map(UrlVisit::toString).toArray(),
                is(new String[]{"url1(25)", "url2(22)", "url4(20)"}));
    }

    private static class UrlVisit {
        private final String url;
        private final int visits;

        public UrlVisit(String url, int visits) {
            this.url = url;
            this.visits = visits;
        }

        public String getUrl() {
            return url;
        }

        public int getVisits() {
            return visits;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UrlVisit urlVisit = (UrlVisit) o;

            return !(url != null ? !url.equals(urlVisit.url) : urlVisit.url != null);
        }

        @Override
        public int hashCode() {
            return url != null ? url.hashCode() : 0;
        }

        @Override
        public String toString() {
            return url + "(" + visits + ")";
        }
    }
}
