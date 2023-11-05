package game.logic.rules;

import game.logic.Rule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpperCaseIncludingRule implements Rule {
    @Override
    public Boolean match(String password) {
        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher passwordMatcher = upperCase.matcher(password);
        return passwordMatcher.find();
    }
    @Override
    public String getDescription() {
        return "Your password must include an uppercase letter";
    }
}
