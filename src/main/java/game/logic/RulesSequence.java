package game.logic;

//Class where are stored all the rules
public class RulesSequence {
    public final static Rule[] sequence = new Rule[] {
            new Rule(RulesLogic::Rule0_isNotEmpty), //<-- control rule: password can't be empty
            new Rule(RulesLogic::Rule1_fiveCharacters),
            new Rule(RulesLogic::Rule2_includesDigits),
            new Rule(RulesLogic::Rule3_includesUpperCase),
            new Rule(RulesLogic::Rule4_includesSpecial)
    };
}
