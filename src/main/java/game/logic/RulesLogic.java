package game.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Class for realization of rules logic
public class RulesLogic {
    public static boolean rule1_fiveCharacters(String password) {
        return password.length() >= 5;
    }
    public static boolean rule2_includesDigits(String password) {
        Pattern digits = Pattern.compile("\\d");
        Matcher passwordMatcher = digits.matcher(password);
        return passwordMatcher.find();
    }
    public static boolean rule3_includesUpperCase(String password) {
        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher passwordMatcher = upperCase.matcher(password);
        return passwordMatcher.find();
    }
    public static boolean rule4_includesSpecial(String password) {
        Pattern specialChars = Pattern.compile("[\\W_]");
        Matcher passwordMatcher = specialChars.matcher(password);
        return passwordMatcher.find();
    }
}