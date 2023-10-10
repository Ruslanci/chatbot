package game.logic;

//Class that provides an interface with RuleSequence.
//checkAllRules returns boolean array, where i-th element equals true if password matches i-th rule
public class RulesChecker {
    private int currentRuleIndex; // <-- в этом поле хранится индекс последнего открытого игроком правила
    public RulesChecker() {
        currentRuleIndex = 0;
    }
    public boolean[] checkAllRules(String password) {
        boolean[] matchedRules = new boolean[currentRuleIndex + 1];
        for (int currentIndex = 0; currentIndex <= currentRuleIndex; currentIndex++) {
            matchedRules[currentIndex] = RulesSequence.sequence[currentIndex].match(password);
        }
        return matchedRules;
    }
    public void getNextRule() {
        currentRuleIndex += (currentRuleIndex < RulesSequence.sequence.length ? 1 : 0);
    }
    public int getCurrentRuleIndex() {
        return currentRuleIndex;
    }
}
