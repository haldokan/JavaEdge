package org.haldokan.edge.interviewquest.facebook;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * My solution to a Facebook interview question. It uses a state machine to do the matching. However it fails for the
 * use cases where a second regex on the right can supplant the one on the left.
 * The Question: 4_STAR
 * TODO: redesign the state machine to remove state from Regex. Handling the failed cases may require a different approach altogether
 * <p>
 * Pattern Matching
 * ----------------
 * Characters: a to z
 * Operators: * +
 * (*) -> matches zero or more (of the character that occurs previous to this operator)
 * (+) -> matches one or more (of the character that occurs previous to this operator)
 * <p>
 * Output if a given pattern matches a string.
 * Example:
 * pattern:a*b
 * string:aaab b, ab, aab, aaab, ab
 * output:1
 * <p>
 * pattern:a+aabc
 * string:ab aabc, aaabc, aaaabc ..
 * output:0
 * <p>
 * pattern:aa*b*ab+
 * string:aab aab, aabab, aaaabbab
 * output:1
 * <p>
 * pattern: a+a*b*
 * string: a ab, aab, aaabb
 * output: 1
 * <p>
 * Valid Assumptions: Assume that both the pattern and string input are valid
 * Created by haytham.aldokanji on 4/23/16.
 */
public class RegexMatching {
    private static final char NONE_OR_MORE = '*';
    private static final char ONE_OR_MORE = '+';
    private static final char NOOP = '$';

    public static void main(String[] args) {
        RegexMatching driver = new RegexMatching();
        driver.test1();
        driver.test2();
        driver.test3();
        driver.test4();
    }

    private static char[] fromStringToCharArr(String string) {
        char[] arr = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            arr[i] = string.charAt(i);
        }
        return arr;
    }

    private Matcher compilePattern(String pattern) {
        char[] patternChars = fromStringToCharArr(pattern);
        int i = pattern.length() - 1;
        Matcher.Builder matcher = Matcher.builder();

        while (i >= 0) {
            char chr = patternChars[i];
            if (isOperator(chr)) {
                //assuming the pattern is well formatted no need to check for indexes
                matcher.add(createRegex(chr, patternChars[--i]));
            } else {
                matcher.add(createRegex(NOOP, chr));
            }
            i--;
        }
        return matcher.build();
    }

    private boolean isOperator(char chr) {
        return chr == NONE_OR_MORE || chr == ONE_OR_MORE;
    }

    private Regex createRegex(char operator, char repeatedChar) {
        if (operator == NONE_OR_MORE) {
            return new NoneOrMoreRegex(repeatedChar);
        } else if (operator == ONE_OR_MORE) {
            return new OneOrMoreRegex(repeatedChar);
        } else {
            return new SingleCharRegex(repeatedChar);
        }
    }

    private void test1() {
        Matcher matcher = compilePattern("a*b");
        assertThat(matcher.match("aaab"), is(true));

        matcher = compilePattern("a*b");
        assertThat(matcher.match("ab"), is(true));

        matcher = compilePattern("a*b");
        assertThat(matcher.match("b"), is(true));

        matcher = compilePattern("a*b");
        assertThat(matcher.match("abb"), is(false));

        matcher = compilePattern("a*b");
        assertThat(matcher.match("bb"), is(false));

        matcher = compilePattern("a*b");
        assertThat(matcher.match("aaa"), is(false));
    }

    private void test2() {
        Matcher matcher = compilePattern("a+b");
        assertThat(matcher.match("aaab"), is(true));

        matcher = compilePattern("a+b");
        assertThat(matcher.match("ab"), is(true));

        matcher = compilePattern("a+b");
        assertThat(matcher.match("b"), is(false));

        matcher = compilePattern("a+b");
        assertThat(matcher.match("bb"), is(false));

        matcher = compilePattern("a+b");
        assertThat(matcher.match("aaa"), is(false));
    }

    private void test3() {
        Matcher matcher = compilePattern("aa*b*ab+");
        assertThat(matcher.match("aaaabbab"), is(true));

        matcher = compilePattern("aa*b*ab+");
        assertThat(matcher.match("aabab"), is(true));

        matcher = compilePattern("aa*b*ab+");
        assertThat(matcher.match("aaaabba"), is(false));

        //fails because of shortages in the logic
        matcher = compilePattern("aa*b*ab+");
//        assertThat(matcher.match("aaab"), is(true));
    }

    private void test4() {
        Matcher matcher = compilePattern("a+a*b*");
        assertThat(matcher.match("aaabb"), is(true));

        // failes
        matcher = compilePattern("aa*b*ab+");
//        assertThat(matcher.match("aab"), is(true));
        // fails
        matcher = compilePattern("aa*b*ab+");
//        assertThat(matcher.match("ab"), is(true));
    }

    private enum Action {STAY, MOVE, MOVE_MATCH, ABORT}

    private abstract static class Regex {
        protected final char repeatedChar;
        protected final char operator;
        protected boolean used;

        public Regex(char operator, char repeatedChar) {
            this.operator = operator;
            this.repeatedChar = repeatedChar;
        }

        public char getOperator() {
            return operator;
        }

        public boolean isUsed() {
            return used;
        }

        public abstract Action matches(char chr);
    }

    private static class NoneOrMoreRegex extends Regex {
        public NoneOrMoreRegex(char repeatedChar) {
            super(NONE_OR_MORE, repeatedChar);
        }

        @Override
        public Action matches(char chr) {
            used = true;
            return chr == repeatedChar ? Action.STAY : Action.MOVE_MATCH;
        }
    }

    private static class OneOrMoreRegex extends Regex {
        private boolean hasToMatch = true;

        public OneOrMoreRegex(char repeatedChar) {
            super(ONE_OR_MORE, repeatedChar);
        }

        @Override
        public Action matches(char chr) {
            used = true;
            Action action = chr == repeatedChar ? Action.STAY : hasToMatch ? Action.ABORT : Action.MOVE_MATCH;
            hasToMatch = false;
            return action;
        }
    }

    private static class SingleCharRegex extends Regex {
        public SingleCharRegex(char repeatedChar) {
            super(NOOP, repeatedChar);
        }

        @Override
        public Action matches(char chr) {
            used = true;
            return chr == repeatedChar ? Action.MOVE : Action.ABORT;
        }
    }

    private static class Matcher {
        private Deque<Regex> regexes;
        private Regex currentRegex;

        private Matcher(Deque<Regex> regexes) {
            this.regexes = new ArrayDeque<>(regexes);
            this.currentRegex = this.regexes.pop();
        }

        public static Builder builder() {
            return new Builder();
        }

        public boolean match(String input) {
            char[] inputArr = fromStringToCharArr(input);
            boolean matched = true;
            for (char chr : inputArr) {
                if (!match(chr)) {
                    matched = false;
                }
            }
            if (!regexCanBeReducedToEmpty()) {
                matched = false;
            }
            return matched;
        }

        private boolean regexCanBeReducedToEmpty() {
            boolean canBeReduced = true;
            if (currentRegex != null && !currentRegex.isUsed() && currentRegex.getOperator() != NONE_OR_MORE) {
                canBeReduced = false;
            }

            while (!regexes.isEmpty()) {
                if (regexes.pop().getOperator() != NONE_OR_MORE) {
                    canBeReduced = false;
                    break;
                }
            }
            return canBeReduced;
        }

        private boolean match(char chr) {
            if (currentRegex == null) {
                return false;
            }
            Action action = currentRegex.matches(chr);
            if (action == Action.ABORT) {
                return false;
            }

            if (action == Action.MOVE) {
                currentRegex();
                return true;

            } else if (action == Action.MOVE_MATCH) {
                currentRegex();
                return match(chr);
            } else {
                return true;
            }
        }

        private void currentRegex() {
            if (!regexes.isEmpty()) {
                currentRegex = regexes.pop();
            } else {
                currentRegex = null;
            }
        }

        private static class Builder {
            Deque<Regex> regexes = new ArrayDeque<>();

            public Builder add(Regex regex) {
                regexes.push(regex);
                return this;
            }

            public Matcher build() {
                return new Matcher(regexes);
            }
        }
    }
}
