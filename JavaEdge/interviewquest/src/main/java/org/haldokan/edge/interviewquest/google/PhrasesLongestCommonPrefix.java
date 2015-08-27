package org.haldokan.edge.interviewquest.google;

import java.util.Arrays;
import java.util.List;

/**
 * My solution to a Google interview question. Similar approach can be implemented iterating over the phrases chars. A
 * more complex approach is using prefix arrays or tries.
 * <p>
 * Find the longest common prefix in a list of phrases. For instance; "i love all dogs", "i love cats" should return
 * "i love".
 *
 * @author haldokan
 */
public class PhrasesLongestCommonPrefix {
    public static void main(String[] args) {
        PhrasesLongestCommonPrefix driver = new PhrasesLongestCommonPrefix();
        List<String> phrases = Arrays.asList(new String[]{"If I had a million dollars", "If I had a chance",
                "If I had married her"});
        System.out.println(driver.longestCommonPrefix(phrases));
    }

    public String longestCommonPrefix(List<String> phrases) {
        if (phrases == null || phrases.isEmpty())
            return null;
        if (phrases.size() == 1)
            return phrases.get(0);

        String[] common = phrases.get(0).split("\\s");

        for (int i = 1; i < phrases.size(); i++) {
            String[] words = phrases.get(i).split("\\s");
            updateCommonPrefix(common, words);
            if (common[0] == null)
                return null;
        }
        return makePrefix(common);
    }

    private void updateCommonPrefix(String[] common, String[] words) {
        for (int i = 0; i < words.length; i++) {
            if (i < common.length && !words[i].equals(common[i]))
                common[i] = null;
        }
    }

    private String makePrefix(String[] common) {
        StringBuilder prefix = new StringBuilder();
        for (String word : common) {
            if (word != null)
                prefix.append(word).append(' ');
            else
                break;
        }
        prefix.deleteCharAt(prefix.length() - 1);
        return prefix.toString();
    }
}
