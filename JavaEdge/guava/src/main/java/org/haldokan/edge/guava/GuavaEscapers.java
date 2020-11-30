package org.haldokan.edge.guava;

import com.google.common.escape.CharEscaper;
import com.google.common.escape.Escaper;
import com.google.common.xml.XmlEscapers;

public class GuavaEscapers {
    public static void main(String[] args) {
        Escaper ae = XmlEscapers.xmlAttributeEscaper();
        Escaper ce = XmlEscapers.xmlContentEscaper();

        String as = ae.escape("attr ='foobar'");
        System.out.println(as);

        String cs = ce.escape("Map<String>");
        System.out.println(cs);

        CharEscaper escaper = new CharEscaper() {
            @Override
            protected char[] escape(char c) {
                if (c == '\'') {
                    return new char[]{'&', 'a', 'p', 'o', 's'};
                }
                return new char[]{c};
            }
        };

        String s = "";
        for (int i = 0; i < 10; i++)
        System.out.println(escaper.escape(s));

    }
}
