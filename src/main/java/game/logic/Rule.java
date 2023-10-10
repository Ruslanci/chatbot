package game.logic;

import java.util.function.Predicate;

//Class that provides work with predicates from string.
public class Rule {
    private final Predicate<String> matchRule;
    public Rule(Predicate<String> matcher) {
        matchRule = matcher;
    }
    public boolean match(String password) {
        return matchRule.test(password);
    }
}