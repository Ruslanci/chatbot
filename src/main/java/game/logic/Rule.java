package game.logic;

import java.util.function.Predicate;

//Class that provides work with predicates from string.
public class Rule {
    public final String Description;
    private final Predicate<String> matchRule;
    public Rule(Predicate<String> iMatcher, String iDescription) {
        matchRule = iMatcher;
        Description = iDescription;
    }
    public Boolean match(String password) {
        return matchRule.test(password);
    }
}