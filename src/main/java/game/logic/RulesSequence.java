package game.logic;

//Class where are stored all the rules
public class RulesSequence {
    public final static Rule[] sequence = new Rule[] {
            new Rule(RulesLogic::rule0_isNotEmpty), //<-- control rule: password can't be empty
            new Rule(RulesLogic::rule1_fiveCharacters),
            new Rule(RulesLogic::rule2_includesDigits),
            new Rule(RulesLogic::rule3_includesUpperCase),
            new Rule(RulesLogic::rule4_includesSpecial)
    };
}
