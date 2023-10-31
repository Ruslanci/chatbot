package game.logic;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class PasswordProcessor {
    private Queue<Rule> qRules;
    List<String> rulesStatus;

    public PasswordProcessor() {
        qRules = RulesSequence.GetRulesQueue();
        rulesStatus = new LinkedList<>();
    }

    public boolean isFinished() {
        return qRules.isEmpty();
    }

    public List<String> processAndGetStatus(String password) {
        rulesStatus.clear();

        for (Rule rule : qRules) {
            boolean ruleSatisfied = rule.match(password);

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