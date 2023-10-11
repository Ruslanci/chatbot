package utils;

import game.logic.RulesChecker;

public class LogicFunctions {
    public static boolean forAll(boolean[] array) {
        for (boolean b : array)
            if (!b) return false;
        return true;
    }
}
