package game.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Class for realization of rules logic
public class RulesLogic {
    public static boolean Rule1_fiveCharacters(String password) {
        return password.length() >= 5;
    }
    public static boolean Rule2_includesDigits(String password) {
        Pattern digits = Pattern.compile("\\d");
        Matcher passwordMatcher = digits.matcher(password);
        return passwordMatcher.find();
    }
    public static boolean Rule3_includesUpperCase(String password) {
        Pattern upperCase = Pattern.compile("[A-Z]");
        Matcher passwordMatcher = upperCase.matcher(password);
        return passwordMatcher.find();
    }
    public static boolean Rule4_includesSpecial(String password) {
        Pattern specialChars = Pattern.compile("[\\W_]");
        Matcher passwordMatcher = specialChars.matcher(password);
        return passwordMatcher.find();
    }
}