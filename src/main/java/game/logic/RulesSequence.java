package game.logic;

import game.logic.rules.*;
import java.util.*;

public class RulesSequence {
    private static Rule[] sequence = new Rule[] {
        new FiveCharactersRule(), new DigitsIncludingRule(), new UpperCaseIncludingRule(), new SpecialIncludingRule()
    };
    static public Queue<Rule> getRulesQueue() {
        return new LinkedList<Rule>(Arrays.asList(sequence));
    }
}