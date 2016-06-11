package org.haldokan.edge.interviewquest.facebook;

/**
 * My solution to a Facebook interview question
 * The Question: 3_STAR
 * Given a string (ex: "ABCSC") Check whether it contains a Substring (ex: "ABC"). If the substring is found remove it
 * and return the remaining string (in our ex "SC").
 */
public class FindingSubString {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static String findAndRemove(String str, String substring) {
        if (str.length() < substring.length()) {
            return str;
        }

        String[] strArr = str.split("");
        String[] substringArr = substring.split("");

        int index = substringIndex(strArr, substringArr);
        if (index < 0) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i < index || i >= index + substring.length()) {
                sb.append(strArr[i]);
            }
        }
        return sb.toString();
    }


    public static int substringIndex(String[] strArr, String[] substringArr) {

        int index = -1;
        for (int i = 0; i < strArr.length; i++) {
            if (i + substringArr.length <= strArr.length) {
                int k = 0;
                for (int j = i; j < i + substringArr.length; j++) {
//                    System.out.println(strArr[j] + "/" + substringArr[k]);
                    if (!strArr[j].equals(substringArr[k++])) {
                        index = -1;
                        break;
                    } else {
                        index = i;
                    }
                }
                if (index > -1) {
                    break;
                }
            }
        }
        return index;
    }

    private static void test1() {
        System.out.println("T->" + substringIndex("xyzabclmn".split(""), "abc".split("")));
        System.out.println("T->" + substringIndex("xyzabclmn".split(""), "mn".split("")));
        System.out.println("T->" + substringIndex("xyzabclmn".split(""), "za".split("")));
        System.out.println("F->" + substringIndex("xyzabclmn".split(""), "bck".split("")));
    }

    private static void test2() {
        System.out.println(findAndRemove("xyzabclmn", "abc"));
        System.out.println(findAndRemove("xyzabclmn", "mn"));
        System.out.println(findAndRemove("xyzabclmn", "za"));
        System.out.println(findAndRemove("xyzabclmn", "bck"));
    }
}
