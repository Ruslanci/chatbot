package game.logic.rules;

import game.logic.Rule;

public class FiveCharactersRule implements Rule {
    @Override
    public Boolean match(String password) {
        return password.length() >= 5;
    }

    @Override
    public String getDescription() {
        return "Your password must be at least 5 characters";
    }
}
