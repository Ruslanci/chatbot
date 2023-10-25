package game.logic;

import java.util.*;

//Class where are stored all the rules
public class RulesSequence { private final static Rule[] sequence = new Rule[] {
        /*new Rule(RulesLogic::rule0_isNotEmpty, "Your password cannot be empty"), //<-- control rule: password can't be empty*/
        new Rule(RulesLogic::rule1_fiveCharacters, "Your password must be at least 5 characters"),
        new Rule(RulesLogic::rule2_includesDigits, "Your password must include a number"),
        new Rule(RulesLogic::rule3_includesUpperCase, "Your password must include an uppercase letter"),
        new Rule(RulesLogic::rule4_includesSpecial, "Your password must include a special character")
};
    static public Queue<Rule> GetRulesQueue() {
        return new LinkedList<Rule>(Arrays.asList(sequence));
    }
}