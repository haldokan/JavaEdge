package org.haldokan.edge.interviewquest.google;

import java.util.*;

/**
 * My solution to a Google interview question. This is an easy question which leads me to think that we have to consider
 * aspects to the question that are not readily available.
 * Given a table of [Url, Content] pairs produce a new table of [Url, Set of duplicate Urls].
 * <p>
 * Example Input:
 * a.com => <html>a</html>
 * b.com => <html>b</html>
 * c.com => <html>c</html>
 * d.com => <html>a</html>
 * e.com => <html>a</html>
 * <p>
 * Example Output:
 * a.com => [d.com, e.com]
 * b.com => []
 * c.com => []
 * Created by haytham.aldokanji on 9/30/15.
 */
public class DuplicateUrls {

    public static void main(String[] args) {
        DuplicateUrls driver = new DuplicateUrls();
        Map<String, String> contentByUrl = new HashMap<>();
        contentByUrl.put("a.com", "a");
        contentByUrl.put("b.com", "b");
        contentByUrl.put("c.com", "c");
        contentByUrl.put("d.com", "a");
        contentByUrl.put("e.com", "a");
        System.out.println(driver.duplicateUrl(contentByUrl));
    }

    public Map<String, Set<String>> duplicateUrl(Map<String, String> contentByUrl) {
        Map<String, Set<String>> urlByContent = new HashMap<>();
        for (String k : contentByUrl.keySet()) {
            String val = contentByUrl.get(k);

            if (!urlByContent.containsKey(val))
                urlByContent.put(val, new HashSet<>());
            urlByContent.get(val).add(k);
        }
        Map<String, Set<String>> dups = new HashMap<>();
        for (Set<String> urls : urlByContent.values()) {
            Iterator<String> it = urls.iterator();
            dups.put(it.next(), urls);
            it.remove();
        }
        return dups;
    }

}
