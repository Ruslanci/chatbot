package game.logic.rules;

import game.logic.Rule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecialIncludingRule implements Rule {
    @Override
    public Boolean match(String password) {
        Pattern specialChars = Pattern.compile("[\\W_]");
        Matcher passwordMatcher = specialChars.matcher(password);
        return passwordMatcher.find();
    }
    @Override
    public String getDescription() {
        return "Your password must include a special character";
    }
}
