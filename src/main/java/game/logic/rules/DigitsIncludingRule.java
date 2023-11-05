package game.logic.rules;

import game.logic.Rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitsIncludingRule implements Rule {
    @Override
    public Boolean match(String password) {
        Pattern digits = Pattern.compile("\\d");
        Matcher passwordMatcher = digits.matcher(password);
        return passwordMatcher.find();
    }

    @Override
    public String getDescription() {
        return "Your password must include a number";
    }
}
