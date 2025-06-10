package org.haldokan.edge.interviewquest.google;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Google interview question - circular array to keep the history
 * The Question: 4_STAR
 * <p>
 * Design a component that will implement web browser history. The user goes to different site and once he presses on
 * history button you should display the last 5 (no duplicates allowed, and 5 can be any N later) if duplicates occur
 * display the most recent one. so if user visit : G,A,B,C,A,Y and then presses "history" we will display Y,A,C,B,G. and
 * of course he can go later to two other websites and then press "history" we will show them then the previous 3.
 * <p>
 * Created by haytham.aldokanji on 6/28/16.
 */
public class BrowsingHistory {
    // circular array -
    private final String[] history;
    private final int displayLen;
    private int lastIndex;

    public BrowsingHistory(int maxHistoryLen, int displayLen) {
        this.history = new String[maxHistoryLen];
        this.displayLen = displayLen;
        this.lastIndex = 0;
    }

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        BrowsingHistory browsingHistory = new BrowsingHistory(5, 3);
        String url1 = "url1", url2 = "url2", url3 = "url3", url4 = "url4", url5 = "url5";

        browsingHistory.addUrl(url1);
        Set<String> history = browsingHistory.history();
        String[] historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url1}));

        browsingHistory.addUrl(url2);
        browsingHistory.addUrl(url3);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url3, url2, url1}));

        browsingHistory.addUrl(url4);
        browsingHistory.addUrl(url5);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url5, url4, url3}));

        browsingHistory.addUrl(url1);
        browsingHistory.addUrl(url4);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url1, url5}));

        browsingHistory.addUrl(url1);
        browsingHistory.addUrl(url4);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url1, url5}));

        browsingHistory.addUrl(url4);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url1}));
    }

    private static void test2() {
        BrowsingHistory browsingHistory = new BrowsingHistory(5, 3);
        String url1 = "url1", url2 = "url2", url3 = "url3", url4 = "url4", url5 = "url5";

        browsingHistory.addUrl(url1);
        browsingHistory.addUrl(url2);
        browsingHistory.addUrl(url3);
        browsingHistory.addUrl(url4);
        Set<String> history = browsingHistory.history();
        String[] historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url3, url2}));

        browsingHistory.removeUrlAtIndex(2);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url2, url1}));

        browsingHistory.removeUrlAtIndex(0);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url4, url2}));

        browsingHistory.removeUrlAtIndex(3);
        history = browsingHistory.history();
        historyArr = history.toArray(new String[0]);
        assertThat(historyArr, is(new String[]{url2}));
    }

    public void addUrl(String url) {
        history[lastIndex] = url;
        lastIndex = (lastIndex + 1) % history.length;
    }

    // assume valid index
    public void removeUrlAtIndex(int index) {
        history[index] = null;
    }

    public Set<String> history() {
        Set<String> recentHistory = new LinkedHashSet<>();
        boolean displayComplete = false;
        for (int i = lastIndex - 1; i >= 0; i--) {
            displayComplete = addToDisplay(recentHistory, i);
            if (displayComplete) {
                break;
            }
        }
        if (!displayComplete) {
            for (int i = history.length - 1; i >= lastIndex; i--) {
                displayComplete = addToDisplay(recentHistory, i);
                if (displayComplete) {
                    break;
                }
            }
        }
        return recentHistory;
    }

    public boolean addToDisplay(Set<String> recentHistory, int urlIndex) {
        boolean displayComplete = false;
        String url = history[urlIndex];

        if (url != null && !recentHistory.contains(url)) {
            recentHistory.add(url);
            if (recentHistory.size() == displayLen) {
                displayComplete = true;
            }
        }
        return displayComplete;
    }
}
