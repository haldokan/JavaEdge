package org.haldokan.edge.dynamicprog;

/**
 * * Solution is a port dynamic programming implementation in C presented by
 * Steven S. Skiena in his book Algorithm Design Manual
 */
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ApproximateStringMatcher {
    private static int MATCH = 0;
    private static int INSERT = 1;
    private static int DELETE = 2;

    private Table<Integer, Integer, Cell> tbl = HashBasedTable.create();

    public static void main(String[] args) {
	ApproximateStringMatcher matcher = new ApproximateStringMatcher();
	String search = "thou shalt not";
	String text = "you should not";
	System.out.println("search: " + search);
	System.out.println("text: " + text);

	int cost = matcher.approxMatch(search, text);
	System.out.println("matching cost: " + cost);

	int[] coords = matcher.goalCell(search, text);
	matcher.path(search, text, coords[0], coords[1]);
    }

    private int approxMatch(String search, String text) {
	int[] op = new int[3];
	initFirstColumn(search);
	initFirstRow(text);

	for (int i = 1; i < search.length(); i++) {
	    for (int j = 1; j < text.length(); j++) {
		op[MATCH] = tbl.get(i - 1, j - 1).cost + match(search.charAt(i), text.charAt(j));
		op[INSERT] = tbl.get(i, j - 1).cost + modify(text.charAt(j));
		op[DELETE] = tbl.get(i - 1, j).cost + modify(search.charAt(i));

		Cell cell = new Cell(op[MATCH], MATCH);
		for (int k = INSERT; k <= DELETE; k++) {
		    if (op[k] < cell.cost) {
			cell.cost = op[k];
			cell.parent = k;
		    }
		}
		tbl.put(i, j, cell);
	    }
	}
	int[] cellCoords = goalCell(search, text);
	return tbl.get(cellCoords[0], cellCoords[1]).cost;
    }

    public void path(String search, String text, int i, int j) {
	Cell cell = tbl.get(i, j);
	if (cell.parent == -1)
	    return;

	if (cell.parent == MATCH) {
	    path(search, text, i - 1, j - 1);
	    if (search.charAt(i) == text.charAt(j))
		System.out.print("M");
	    else
		System.out.print("S");

	    return;
	}
	if (cell.parent == INSERT) {
	    path(search, text, i, j - 1);
	    System.out.print("I");
	    return;
	}
	if (cell.parent == DELETE) {
	    path(search, text, i - 1, j);
	    System.out.print("D");
	    return;
	}
    }

    private int[] goalCell(String search, String text) {
	return new int[] { search.length() - 1, text.length() - 1 };
    }

    private int modify(char c) {
	return 1;
    }

    private int match(char c1, char c2) {
	return c1 == c2 ? 0 : 1;
    }

    private void initFirstRow(String text) {
	for (int i = 0; i < text.length(); i++) {
	    int parent = i > 0 ? INSERT : -1;
	    tbl.put(0, i, new Cell(i, parent));
	}
    }

    private void initFirstColumn(String search) {
	for (int i = 0; i < search.length(); i++) {
	    int parent = i > 0 ? DELETE : -1;
	    tbl.put(i, 0, new Cell(i, parent));
	}
    }

    private static class Cell {
	private int cost;
	private int parent;

	public Cell(int cost, int parent) {
	    this.cost = cost;
	    this.parent = parent;
	}
    }
}