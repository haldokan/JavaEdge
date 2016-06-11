package org.haldokan.edge.interviewquest.linkedin;

/**
 * My solution to a Linkedin interview question
 * The Question: 3_STAR
 * Given a string , "This is a test" reverse it: "tset a si sihT" Do this recursively.
 *
 * @author haldokan
 */
public class ReverseStringRecursively {
    public static void main(String[] args) {
        ReverseStringRecursively driver = new ReverseStringRecursively();
        // reverse
        String s = driver.reverse("This is a test");
        System.out.println(s);
        // reverse it back to English
        System.out.println(driver.reverse(s));
    }

    public String reverse(String s) {
        if (s == null)
            throw new IllegalArgumentException("Null input");
        if (s.isEmpty())
            return s;
        return s.substring(s.length() - 1, s.length()) + reverse(s.substring(0, s.length() - 1));
    }
}
