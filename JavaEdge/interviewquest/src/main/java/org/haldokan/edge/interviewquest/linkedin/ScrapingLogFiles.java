package org.haldokan.edge.interviewquest.linkedin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * My solution to a Linkedin interview question. Running the program against the file pasted on the bottom of the class
 * produces this output:
 * <p>
 * { autosuggest_backend : solrsearch10_01{212:2, 200:2, 526:2} }
 * <p>
 * { autofeedback_backend : solrsearch10_01{200:1, 333:1, 526:1} }
 * <p>
 * { navigtracking_backend : solrsearch10_01{212:1, 200:1, 526:1} }
 * <p>
 * { recommendshare_backend : fe02{246:1, 200:1, 3291:1} }
 * <p>
 * { imageshare_backend : fe01{451:1, 200:1, 296:1} }
 * <p>
 * { slideshare_backend : fe02{246:3, 999:1, 200:3, 3291:2} fe01{451:3, 200:2, 296:3, 777:1} }
 * <p>
 * Program to count all Response codes individually per database (slideshare_backend_fe01 is one db) in the given log
 * file
 * <p>
 * output should be like this { backend : backend name { 200: 20 503: 3 where 3 is count of response codes 503 } }
 * <p>
 * Logfile looks like this
 * <p>
 * May 29 13:53:13 127.0.0.1 haproxy[27326]: 164.85.131.129:15592 [29/May/2013:13:53:13.671] slideshare
 * slideshare_backend/fe01 1/0/1/106/296 200 451 - - --VN 1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (Windows NT 5.1; rv:10.0.4) Gecko/20100101
 * Firefox/10.0.4|http://www.slideshare.net/slideshow/embed_code/11959978?hostedIn
 * =slideshare&referer=http://www.slideshare.net/goulart.sousa|}
 * {2471317690|||pingback/embed_or_homepageplayerhits|s11959978/a8717995|} "GET
 * /pingback/embed_or_homepageplayerhits/11959978?ref=http%3A%2F%2Fwww.slideshare.net %2Fgoulart.sousa&_=1369853592559
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 217.129.26.81:50910 [29/May/2013:13:53:13.724] slideshare
 * slideshare_backend/fe02 17/0/0/1/246 200 3291 - - --VN 1529/1529/839/435/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)|http://www.slideshare.net/inexita/4|} {1098476889
 * 1090975733|||||} "GET /images/fadedlogo.jpg?829e162373dc3b1cc145ba5b62aba32c8c804b67 HTTP/1.1" May 29 13:53:13
 * 127.0.0.1 haproxy[27326]: 79.154.237.97:50310 [29/May/2013:13:53:13.758] slideshare
 * autosuggest_backend/solrsearch10_01 13/0/0/3/212 200 526 - - ---- 1528/1528/17/3/0 0/0
 * {autosuggest.slideshare.net|Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
 * Chrome/27.0.1453.94
 * Safari/537.36|http://www.slideshare.net/faroviejo/las-10-ciudades-ms-grandes-del-mundo-y-la-paz-bcs|} {|||||}
 * "GET /?q=las?10?ciudades?ma*&rows=5&wt=json&sort=frequency%20desc&fq=%2Bresults%3A%5B10%20TO%20*%5D&json.wrf=jQuery17209988682114053518_1369853526684&_=1369853590254 HTTP/1.1"
 *
 * @author haldokan
 */
public class ScrapingLogFiles {
    private Map<String, Set<String>> dbsByBackend = new HashMap<>();
    private Map<String, DBResponseCodes> responseCodesByDb = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ScrapingLogFiles driver = new ScrapingLogFiles();

        // String line = "slideshare_backend/fe01 1/0/1/106/296 200 451 - - --VN "
        // + "1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0";
        // System.out.println(driver.getBackendName(line).get());
        // System.out.println(driver.getDatabaseName(line).get());
        // System.out.println(driver.getResponseCodes(line));
        // driver.scrapeLine(line);

        driver.scrapeLogFile("c:\\temp\\testfile.txt");
        driver.printScapedData();
    }

    public void scrapeLogFile(String fileName) throws IOException {
        // we may want to read the file one line at a time instead, however we read it all here for simplicity
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (String line : lines)
            scrapeLine(line);
    }

    private void printScapedData() {
        for (String backend : dbsByBackend.keySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append("{ " + backend + " : ");
            for (String db : dbsByBackend.get(backend)) {
                sb.append(responseCodesByDb.get(dbQualifiedName(backend, db)));
            }
            sb.append("}\n");
            System.out.print(sb.toString());
        }
    }

    private void scrapeLine(String line) {
        Optional<String> backend = getBackendName(line);
        if (backend.isPresent()) {
            String backendName = backend.get();
            dbsByBackend.computeIfAbsent(backendName, k -> new HashSet<String>());
            String dbName = getDatabaseName(line).get();
            dbsByBackend.get(backendName).add(dbName);

            String dbQualifiedName = dbQualifiedName(backendName, dbName);
            List<Integer> responseCodes = getResponseCodes(line);
            DBResponseCodes codesForDb = responseCodesByDb.get(dbQualifiedName);
            if (codesForDb == null)
                responseCodesByDb.put(dbQualifiedName, new DBResponseCodes(dbName, responseCodes));
            else
                codesForDb.addCodes(responseCodes);
        }
    }

    private String dbQualifiedName(String backendName, String dbName) {
        return backendName + "_" + dbName;
    }

    private Optional<String> getBackendName(String line) {
        int firstSlashNdx = line.indexOf('/');
        if (firstSlashNdx != -1) {
            String backend = line.substring(0, firstSlashNdx);
            if (backend.matches("\\w*_backend")) {
                return Optional.of(backend);
            }
        }
        return Optional.empty();
    }

    private Optional<String> getDatabaseName(String line) {
        return Optional.of(line.substring(line.indexOf('/') + 1, line.indexOf(' ')));
    }

    private List<Integer> getResponseCodes(String line) {
        int ndxSpaceAfterCodes = line.indexOf(' ', line.indexOf(' ') + 1);
        int ndxDash = line.indexOf('-', ndxSpaceAfterCodes);
        String[] codes = line.substring(line.lastIndexOf('/', ndxSpaceAfterCodes) + 1, ndxDash).split(" ");

        List<Integer> codesList = new ArrayList<>();
        for (String code : codes) {
            codesList.add(Integer.valueOf(code));
        }
        return codesList;
    }

    private static class DBResponseCodes {
        private final String dbName;
        private Map<Integer, Integer> codeCountByCode = new HashMap<>();

        public DBResponseCodes(String dbName, List<Integer> codes) {
            this.dbName = dbName;
            addCodes(codes);
        }

        public void addCodes(List<Integer> codes) {
            for (Integer code : codes)
                codeCountByCode.compute(code, (k, v) -> v == null ? 1 : v + 1);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(dbName).append("{");
            for (Integer code : codeCountByCode.keySet()) {
                sb.append(code).append(":").append(codeCountByCode.get(code)).append(", ");
            }
            if (sb.length() > 1)
                sb.delete(sb.length() - 2, sb.length());
            sb.append("} ");
            return sb.toString();
        }
    }
}

/*
 * May 29 13:53:13 127.0.0.1 haproxy[27326]: 164.85.131.129:15592 [29/May/2013:13:53:13.671] slideshare
 * slideshare_backend/fe01 1/0/1/106/296 200 451 - - --VN 1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (Windows NT 5.1; rv:10.0.4) Gecko/20100101
 * Firefox/10.0.4|http://www.slideshare.net/slideshow/embed_code/11959978?hostedIn
 * =slideshare&referer=http://www.slideshare.net/goulart.sousa|}
 * {2471317690|||pingback/embed_or_homepageplayerhits|s11959978/a8717995|} "GET
 * /pingback/embed_or_homepageplayerhits/11959978?ref=http%3A%2F%2Fwww.slideshare.net %2Fgoulart.sousa&_=1369853592559
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 217.129.26.81:50910 [29/May/2013:13:53:13.724] slideshare
 * slideshare_backend/fe02 17/0/0/1/246 200 3291 - - --VN 1529/1529/839/435/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)|http://www.slideshare.net/inexita/4|} {1098476889
 * 1090975733|||||} "GET /images/fadedlogo.jpg?829e162373dc3b1cc145ba5b62aba32c8c804b67 HTTP/1.1" May 29 13:53:13
 * 127.0.0.1 haproxy[27326]: 79.154.237.97:50310 [29/May/2013:13:53:13.758] slideshare
 * autosuggest_backend/solrsearch10_01 13/0/0/3/212 200 526 - - ---- 1528/1528/17/3/0 0/0
 * {autosuggest.slideshare.net|Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
 * Chrome/27.0.1453.94
 * Safari/537.36|http://www.slideshare.net/faroviejo/las-10-ciudades-ms-grandes-del-mundo-y-la-paz-bcs|} {|||||} "GET
 * /?q=las?10?ciudades?ma*&rows=5&wt=json&sort=frequency%20desc&fq=%2Bresults%3A%5B10%20TO%20*%5D&json.wrf=
 * jQuery17209988682114053518_1369853526684&_=1369853590254
 * 
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 164.85.131.129:15592 [29/May/2013:13:53:13.671] slideshare
 * slideshare_backend/fe01 1/0/1/106/296 200 451 - - --VN 1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (Windows NT 5.1; rv:10.0.4) Gecko/20100101
 * Firefox/10.0.4|http://www.slideshare.net/slideshow/embed_code/11959978?hostedIn
 * =slideshare&referer=http://www.slideshare.net/goulart.sousa|}
 * {2471317690|||pingback/embed_or_homepageplayerhits|s11959978/a8717995|} "GET
 * /pingback/embed_or_homepageplayerhits/11959978?ref=http%3A%2F%2Fwww.slideshare.net %2Fgoulart.sousa&_=1369853592559
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 217.129.26.81:50910 [29/May/2013:13:53:13.724] slideshare
 * slideshare_backend/fe02 17/0/0/1/246 200 3291 - - --VN 1529/1529/839/435/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)|http://www.slideshare.net/inexita/4|} {1098476889
 * 1090975733|||||} "GET /images/fadedlogo.jpg?829e162373dc3b1cc145ba5b62aba32c8c804b67 HTTP/1.1" May 29 13:53:13
 * 127.0.0.1 haproxy[27326]: 79.154.237.97:50310 [29/May/2013:13:53:13.758] slideshare
 * autosuggest_backend/solrsearch10_01 13/0/0/3/212 200 526 - - ---- 1528/1528/17/3/0 0/0
 * {autosuggest.slideshare.net|Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
 * Chrome/27.0.1453.94
 * Safari/537.36|http://www.slideshare.net/faroviejo/las-10-ciudades-ms-grandes-del-mundo-y-la-paz-bcs|} {|||||} "GET
 * /?q=las?10?ciudades?ma*&rows=5&wt=json&sort=frequency%20desc&fq=%2Bresults%3A%5B10%20TO%20*%5D&json.wrf=
 * jQuery17209988682114053518_1369853526684&_=1369853590254
 * 
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 164.85.131.129:15592 [29/May/2013:13:53:13.671] slideshare
 * slideshare_backend/fe01 1/0/1/106/296 777 451 - - --VN 1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (Windows NT 5.1; rv:10.0.4) Gecko/20100101
 * Firefox/10.0.4|http://www.slideshare.net/slideshow/embed_code/11959978?hostedIn
 * =slideshare&referer=http://www.slideshare.net/goulart.sousa|}
 * {2471317690|||pingback/embed_or_homepageplayerhits|s11959978/a8717995|} "GET
 * /pingback/embed_or_homepageplayerhits/11959978?ref=http%3A%2F%2Fwww.slideshare.net %2Fgoulart.sousa&_=1369853592559
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 217.129.26.81:50910 [29/May/2013:13:53:13.724] slideshare
 * slideshare_backend/fe02 17/0/0/1/246 200 999 - - --VN 1529/1529/839/435/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)|http://www.slideshare.net/inexita/4|} {1098476889
 * 1090975733|||||} "GET /images/fadedlogo.jpg?829e162373dc3b1cc145ba5b62aba32c8c804b67 HTTP/1.1" May 29 13:53:13
 * 127.0.0.1 haproxy[27326]: 79.154.237.97:50310 [29/May/2013:13:53:13.758] slideshare
 * autofeedback_backend/solrsearch10_01 13/0/0/3/333 200 526 - - ---- 1528/1528/17/3/0 0/0
 * {autosuggest.slideshare.net|Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
 * Chrome/27.0.1453.94
 * Safari/537.36|http://www.slideshare.net/faroviejo/las-10-ciudades-ms-grandes-del-mundo-y-la-paz-bcs|} {|||||} "GET
 * /?q=las?10?ciudades?ma*&rows=5&wt=json&sort=frequency%20desc&fq=%2Bresults%3A%5B10%20TO%20*%5D&json.wrf=
 * jQuery17209988682114053518_1369853526684&_=1369853590254
 * 
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 164.85.131.129:15592 [29/May/2013:13:53:13.671] slideshare
 * imageshare_backend/fe01 1/0/1/106/296 200 451 - - --VN 1526/1526/837/402/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (Windows NT 5.1; rv:10.0.4) Gecko/20100101
 * Firefox/10.0.4|http://www.slideshare.net/slideshow/embed_code/11959978?hostedIn
 * =slideshare&referer=http://www.slideshare.net/goulart.sousa|}
 * {2471317690|||pingback/embed_or_homepageplayerhits|s11959978/a8717995|} "GET
 * /pingback/embed_or_homepageplayerhits/11959978?ref=http%3A%2F%2Fwww.slideshare.net %2Fgoulart.sousa&_=1369853592559
 * HTTP/1.1" May 29 13:53:13 127.0.0.1 haproxy[27326]: 217.129.26.81:50910 [29/May/2013:13:53:13.724] slideshare
 * recommendshare_backend/fe02 17/0/0/1/246 200 3291 - - --VN 1529/1529/839/435/0 0/0 {www.slideshare.net|Mozilla/5.0
 * (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)|http://www.slideshare.net/inexita/4|} {1098476889
 * 1090975733|||||} "GET /images/fadedlogo.jpg?829e162373dc3b1cc145ba5b62aba32c8c804b67 HTTP/1.1" May 29 13:53:13
 * 127.0.0.1 haproxy[27326]: 79.154.237.97:50310 [29/May/2013:13:53:13.758] slideshare
 * navigtracking_backend/solrsearch10_01 13/0/0/3/212 200 526 - - ---- 1528/1528/17/3/0 0/0
 * {autosuggest.slideshare.net|Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
 * Chrome/27.0.1453.94
 * Safari/537.36|http://www.slideshare.net/faroviejo/las-10-ciudades-ms-grandes-del-mundo-y-la-paz-bcs|} {|||||} "GET
 * /?q=las?10?ciudades?ma*&rows=5&wt=json&sort=frequency%20desc&fq=%2Bresults%3A%5B10%20TO%20*%5D&json.wrf=
 * jQuery17209988682114053518_1369853526684&_=1369853590254
 * HTTP/1.1"
 */