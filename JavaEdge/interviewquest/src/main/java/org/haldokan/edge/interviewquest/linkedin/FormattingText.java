package org.haldokan.edge.interviewquest.linkedin;

/**
 * My solution to a Linkedin interview question. There must be a better way to solve it. My solution *almost* works (one
 * space indentation is added occasionally). However, I think manipulating the text in a StringBuilder is a valid
 * approach.
 * 
 * Given a long text, print the formatted text such that each line has at most L characters and the text is left-right
 * justified. No breaking of words are allowed.
 * 
 * @author haldokan
 *
 */
public class FormattingText {

    public static void main(String[] args) {
	String text = "Once upon a time I was playing in the bushes on the outskirts of the village. "
		+ "Shrubs and fig trees and bees; it was heaven! But all of a sudden I saw a snake. "
		+ "it slithered away in continuous and supple twists. I was not afraid and I stood there watching. "
		+ "To this day I am not afraid of snakes. What I am really afraid of are those crawlers with many legs! "
		+ "They do freak me out! What does trigger your fears?";
	FormattingText driver = new FormattingText();
	driver.printFormatted(text, 50);
    }

    public void printFormatted(String text, int lineLen) {
	if (text.length() <= lineLen) {
	    System.out.println(text);
	    return;
	}
	StringBuilder editor = new StringBuilder(text);
	int ndx = 1;
	for (;;) {
	    int prevNdx = ndx;
	    ndx += lineLen;
	    if (ndx >= editor.length()) {
		ndx = ndx - (ndx - editor.length() + 1);
		trim(editor, prevNdx, ndx);
		editor.append('\n');
		System.out.println(editor.toString());
		break;
	    } else if (isFullWord(editor, ndx)) {
		ndx = trim(editor, prevNdx, ndx);
		editor.insert(ndx + 1, '\n');
		ndx++;
	    } else {
		ndx = trim(editor, prevNdx, ndx);
		int spaceNdx = findIndexOfFirstSpaceBackwards(editor, ndx, lineLen);
		if (spaceNdx == -1)
		    throw new IllegalStateException("Word length is greater than max line length");
		editor.replace(spaceNdx, spaceNdx + 1, "\n");
		ndx = spaceNdx;
	    }
	}

    }

    private boolean isFullWord(StringBuilder editor, int ndx) {
	if (isWordEnd(editor.charAt(ndx)))
	    return true;
	if (ndx == editor.length() - 1)
	    return true;
	return editor.charAt(ndx + 1) == ' ';
    }

    private boolean isWordEnd(char c) {
	return c == ' ' || c == '.' || c == ',' || c == ';' || c == '!' || c == '\n';
    }

    private int trim(StringBuilder editor, int start, int end) {
	int ndx1 = end;
	for (;;) {
	    if (editor.charAt(start) == ' ') {
		editor.delete(start, start + 1);
		ndx1--;
	    } else {
		break;
	    }
	}
	int ndx2 = end;
	for (;;) {
	    if (editor.charAt(ndx2) == ' ') {
		editor.delete(ndx2, ndx2 + 1);
		ndx2--;
		ndx1--;
	    } else {
		break;
	    }
	}
	return ndx1;
    }

    private int findIndexOfFirstSpaceBackwards(StringBuilder editor, int ndx, int lineLen) {
	for (int i = ndx; i >= ndx - lineLen; i--) {
	    if (editor.charAt(i) == ' ') {
		return i;
	    }
	}
	return -1;
    }
}
