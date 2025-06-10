package org.haldokan.edge.interviewquest.amazon;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * NOTE: this is maybe a faulty solution
 * My solution to an Amazon interview question (in fact it is a Black Rock interview question but somewhat similar to another
 * Amazon question that I resolved elsewhere in this repository).
 * <p>
 * The Question: 4_STAR
 * <p>
 * A fund manager has a set of portfolios. Any portfolio can have 2 sub portfolios linked to it (binary tree). Each portfolio
 * has an int value that represent it's available funds for investment. To minimize risk the fund manager cannot invest
 * 2 directly linked portfolios (parent to child). Find the maximum amount the fund manager can invest w/o violating the risk
 * stipulation. The set of portfolios is given as a string in level order traversal of the binary tree. Null portfolio nodes
 * are represented by #.
 * <p>
 * Example: 3 4 5 1 1 # 1 # 7 # # 5 8
 * represents portfolio binary tree with max investment = 29.
 * <p>
 * **********************3
 * ***************4           5
 * ***********1       1           1
 * *************7               5   8
 * <p>
 * Created by haytham.aldokanji on 8/15/16.
 */
public class MaxPortfolioInvestment {
    public static void main(String[] args) {
        MaxPortfolioInvestment driver = new MaxPortfolioInvestment();
        driver.test();
    }

    public int findMax(String tree) {
        String[] portfolios = tree.split(" ");
        Deque<Integer> evalDeck = new ArrayDeque<>();
        Set<Integer> selectedPortfolios = new HashSet<>();

        int index = 0;
        evalDeck.add(index);
        selectedPortfolios.add(index);

        int maxInvestment = Integer.parseInt(portfolios[index]);

        while (!evalDeck.isEmpty()) {
            Integer parentIndex = evalDeck.remove();
            int childPortfoliosSum = 0;

            Optional<Integer> child1Funds = evaluatePortfolio(portfolios, ++index);
            if (child1Funds.isPresent()) {
                evalDeck.add(index);
                childPortfoliosSum += child1Funds.get();
            }

            Optional<Integer> child2Funds = evaluatePortfolio(portfolios, ++index);
            if (child2Funds.isPresent()) {
                evalDeck.add(index);
                childPortfoliosSum += child2Funds.get();
            }

            int parentPortfolioFunds = selectedPortfolios.contains(parentIndex) ?
                    Integer.parseInt(portfolios[parentIndex]) : 0;

            if (childPortfoliosSum > parentPortfolioFunds) {
                maxInvestment = maxInvestment - parentPortfolioFunds + childPortfoliosSum;
                selectedPortfolios.remove(parentIndex); // may be not there but it doesn't matter

                if (child1Funds.isPresent()) {
                    selectedPortfolios.add(index - 1);
                }
                if (child2Funds.isPresent()) {
                    selectedPortfolios.add(index);
                }
            }
        }
        return maxInvestment;
    }

    private Optional<Integer> evaluatePortfolio(String[] portfolios, int index) {
        Integer funds = null;
        if (index < portfolios.length) {
            String itemAsString = portfolios[index];
            if (!itemAsString.equals("#")) {
                funds = Integer.valueOf(itemAsString);
            }
        }
        return Optional.ofNullable(funds);
    }

    private void test() {
        int max = findMax("3 4 5 1 1 # 1 # 7 # # 5 8");
        assertThat(max, is(29));

        max = findMax("3 4 5 1 1 # 6 # 7 # # 5 8");
        assertThat(max, is(24));

        max = findMax("3 4 5 1 1 # 6 # 7 # # 2 3");
        assertThat(max, is(17));

        max = findMax("3 4 5 1 # # 1 # 7 # # 5 8");
        assertThat(max, is(29));
    }
}
