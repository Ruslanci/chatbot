package game.logic;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class PasswordProcessor {
    final private Queue<Rule> qRules;
    List<String> rulesStatus;

    public PasswordProcessor() {
        qRules = RulesSequence.GetRulesQueue();
        rulesStatus = new LinkedList<>();
    }

    public List<String> ProcessAndGetStatus(String password) {
        rulesStatus.clear();

        for (Rule rule : qRules) {
            boolean ruleSatisfied = rule.Match(password);

            rulesStatus.add((ruleSatisfied ? "+ " : "- ") + rule.description);
        }

        return rulesStatus;
    }

    public boolean RulesSatisfied() {
        for (String str : rulesStatus) {
            if (str.contains("-")) {
                return false;
            }
        }
        return true;
    }
}