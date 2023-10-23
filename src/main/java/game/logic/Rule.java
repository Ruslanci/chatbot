package game.logic;

import java.util.function.Predicate;

//Class that provides work with predicates from string.
public class Rule {
    public final String description;
    private final Predicate<String> matchRule;
    public Rule(Predicate<String> iMatcher, String iDescription) {
        matchRule = iMatcher;
        description = iDescription;
    }
    public boolean match(String password) {
        return matchRule.test(password);
    }
}